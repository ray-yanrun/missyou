package com.lin.missyou.service;

import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.core.money.MoneyDiscount;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.logic.CouponChecker;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.*;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.OrderRepository;
import com.lin.missyou.repository.SkuRepository;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private MoneyDiscount moneyDiscount;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${missyou.order.maxSkuLimit}")
    private int maxSkuLimit;
    @Value("${missyou.order.payTimeLimit}")
    private int payTimeLimit;

    @Override
    public OrderChecker isOk(Long uid, OrderDTO orderDTO) {
        List<Long> skuIdList = orderDTO.getSkuInfoDTOList().stream()
                .map(SkuInfoDTO::getId)
                .collect(Collectors.toList());

        List<Sku> skuList = skuService.getSkuListByIdList(skuIdList);

        // couponCheck
        Long couponId = orderDTO.getCouponId();
        CouponChecker couponChecker = null;
        if(couponId != null){
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFoundException(40004));
            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndCouponIdAndStatus(uid, couponId, 1)
                    .orElseThrow(()->new NotFoundException(50006));
            couponChecker = new CouponChecker(coupon, moneyDiscount);
        }

        OrderChecker orderChecker = new OrderChecker(orderDTO, skuList, couponChecker, maxSkuLimit);
        orderChecker.isOK();
        return orderChecker;
    }

    @Override
    @Transactional
    public Long placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker) {
        String orderNo = OrderUtil.makeOrderNo();
        Calendar now = Calendar.getInstance();
        Calendar now1 = (Calendar) now.clone();
        Date expiredTime = CommonUtil.addSomeSeconds(now, this.payTimeLimit).getTime();
        Order order = Order.builder()
                .orderNo(orderNo)
                .totalPrice(orderDTO.getTotalPrice())
                .finalTotalPrice(orderDTO.getFinalTotalPrice())
                .userId(uid)
                .totalCount(orderChecker.getTotalCount())
                .snapImg(orderChecker.getLeaderImg())
                .snapTitle(orderChecker.getLeaderTitle())
                .status(OrderStatus.UNPAID.value())
                .expiredTime(expiredTime)
                .placedTime(now1.getTime())
                .build();
        order.setSnapAddress(orderDTO.getAddress());
        order.setSnapItems(orderChecker.getOrderSkuList());
        order.setCreateTime(now1.getTime());
        this.orderRepository.save(order);
        // 扣减库存
        this.reduceStock(orderChecker);
        // 核销优惠券
        Long couponId = -1L;  // 如果订单没有优惠券，默认值为-1
        if(orderDTO.getCouponId() != null){
            this.writeOffCoupon(orderDTO.getCouponId(), order.getId(), uid);
            couponId = orderDTO.getCouponId();
        }
        // todo 延迟队列 通知
        sendToRedis(order.getId(), uid, couponId);



        return order.getId();
    }

    @Override
    public Page<Order> getUnpaid(Integer page, Integer size) {
        Long uid = LocalUser.getUser().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Date now = new Date();
        return this.orderRepository.findByExpiredTimeGreaterThanAndStatusAndUserId(now, OrderStatus.UNPAID.value(), uid, pageable);
    }

    @Override
    public Page<Order> getByStatus(Integer status, Integer page, Integer size) {
        Long uid = LocalUser.getUser().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        if(status == OrderStatus.All.value()){
            return orderRepository.findByUserId(uid, pageable);
        }
        return orderRepository.findByUserIdAndStatus(status, uid, pageable);
    }

    @Override
    public Optional<Order> getOrderDetail(Long oid){
        Long uid = LocalUser.getUser().getId();
        return this.orderRepository.findByUserIdAndId(uid, oid);
    }

    @Override
    public void updateOrderPrepayId(Long orderId, String prepayId) {
        Optional<Order> order = orderRepository.findById(orderId);
        order.ifPresent((o)->{
            o.setPrepayId(prepayId);
            orderRepository.save(o);
        });
        order.orElseThrow(() -> new ParameterException(10007));
    }

    // 乐观锁 扣减库存
    private void reduceStock(OrderChecker orderChecker){
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for(OrderSku orderSku:orderSkuList){
            int result = skuRepository.reduceStock(orderSku.getId(), orderSku.getCount());
            if(result != 1){
                throw new ParameterException(50003);
            }
        }
    }

    private void writeOffCoupon(Long couponId, Long oid, Long uid){
        int result = userCouponRepository.writeOff(couponId, oid, uid);
        if(result != 1){
            throw new ForbiddenException(40012);
        }
    }

    // 订单超时未支付 归还库存和优惠券
    private void sendToRedis(Long oid, Long uid, Long couponId){
        String key = oid.toString()+","+uid.toString()+","+couponId.toString();
        // 将订单相关信息设置为redis的键，并进行订阅
        try{
            stringRedisTemplate.opsForValue().set(key, "1", payTimeLimit, TimeUnit.SECONDS);
        }catch(Exception e){
            e.printStackTrace();  // 此时不要抛出异常，否则整个事务会回滚影响下单
        }

    }




}
