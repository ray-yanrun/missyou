package com.lin.missyou.logic;

import com.lin.missyou.bo.SkuOrderBO;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.dto.SkuInfoDTO;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.OrderSku;
import com.lin.missyou.model.Sku;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderChecker {

    private OrderDTO orderDTO;
    private List<Sku> skuList;
    private CouponChecker couponChecker;
    private Integer maxSkuLimit;

    @Getter
    private List<OrderSku> orderSkuList = new ArrayList<>();

    public OrderChecker(OrderDTO orderDTO, List<Sku> skuList,
                        CouponChecker couponChecker, Integer maxSkuLimit){
        this.orderDTO = orderDTO;
        this.skuList = skuList;
        this.couponChecker = couponChecker;
        this.maxSkuLimit = maxSkuLimit;
    }

    public void isOK(){

        BigDecimal serverTotalPrice = new BigDecimal("0");
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();

        // 校验商品是否被下架
        this.skuNotOnSale(orderDTO.getSkuInfoDTOList().size(), skuList.size());
        // 校验商品是否售罄
        for(int i=0;i<orderDTO.getSkuInfoDTOList().size();i++){
            Sku sku = skuList.get(i);
            SkuInfoDTO skuInfoDTO = orderDTO.getSkuInfoDTOList().get(i);
            this.containSoldOutSku(sku);
            this.beyondSkuStock(sku, skuInfoDTO);
            this.beyondSkuLimit(skuInfoDTO);
            serverTotalPrice = serverTotalPrice.add(this.calculateSkuOrderPrice(sku, skuInfoDTO));
            skuOrderBOList.add(new SkuOrderBO(sku, skuInfoDTO));
            orderSkuList.add(new OrderSku(skuInfoDTO, sku));
        }

        // 校验前后端价格是否相等
        this.totalPriceIsOK(orderDTO.getTotalPrice(), serverTotalPrice);
        // 校验优惠券
        if(couponChecker != null){
            this.couponChecker.isOk();
            this.couponChecker.canBeUsed(skuOrderBOList, serverTotalPrice);
            this.couponChecker.finalTotalPriceIsOk(orderDTO.getTotalPrice(), serverTotalPrice);
        }
    }

    // 下单
    public void placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker){

    }

    // 当前端传来的数据和服务端查询的数据不相等，则说明有sku被下架了
    private void skuNotOnSale(int count1, int count2){
        if(count1 != count2){
            throw new ParameterException(50002);
        }
    }

    // 预判断当前Sku是否已经售罄
    private void containSoldOutSku(Sku sku){
        if(sku.getStock() == 0){
            throw new ParameterException(50001);
        }
    }

    // 当前sku的库存小于前端传进来的购买数量 抛出异常
    private void beyondSkuStock(Sku sku, SkuInfoDTO skuInfoDTO){
        if(sku.getStock() < skuInfoDTO.getCount()){
            throw new ParameterException(50003);
        }
    }

    // 当前商品购买数量是否大于限购最大值
    private void beyondSkuLimit(SkuInfoDTO skuInfoDTO){
        if(skuInfoDTO.getCount() > this.maxSkuLimit){
            throw new ParameterException(50004);
        }
    }

    // 计算订单商品总金额
    private BigDecimal calculateSkuOrderPrice(Sku sku, SkuInfoDTO skuInfoDTO){
        if(skuInfoDTO.getCount() <= 0){
            throw new ParameterException(50007);
        }
        return sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
    }

    // 比较两个价格是否相等
    private void totalPriceIsOK(BigDecimal orderTotalPrice, BigDecimal serverTotalPrice){
        if(orderTotalPrice.compareTo(serverTotalPrice) != 0){
            throw new ParameterException(50005);
        }
    }

    // 获取一组sku的第一个图片作为订单的图片
    public String getLeaderImg(){
        return this.skuList.get(0).getImg();
    }

    public String getLeaderTitle(){
        return this.skuList.get(0).getTitle();
    }

    public Integer getTotalCount(){
        return this.orderDTO.getSkuInfoDTOList().stream()
                .map(SkuInfoDTO::getCount)
                .reduce(Integer::sum)
                .orElse(0);
    }
}
