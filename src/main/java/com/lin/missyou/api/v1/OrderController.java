package com.lin.missyou.api.v1;

import com.lin.missyou.bo.PageCounter;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.core.interceptors.ScopeLevel;
import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.Order;
import com.lin.missyou.service.OrderService;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.vo.OrderIdVO;
import com.lin.missyou.vo.OrderPureVO;
import com.lin.missyou.vo.OrderSimplifyVO;
import com.lin.missyou.vo.PagingDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${missyou.order.payTimeLimit}")
    private int payTimeLimit;

    @ScopeLevel
    @PostMapping("")
    public OrderIdVO placeOrder(@RequestBody OrderDTO orderDto){
        Long uid = LocalUser.getUser().getId();
        // 订单校验
        OrderChecker orderChecker = orderService.isOk(uid, orderDto);

        // 下单
        Long oid = orderService.placeOrder(uid, orderDto, orderChecker);
        return new OrderIdVO(oid);
    }

    @ScopeLevel
    @GetMapping("/status/unpaid")
    @SuppressWarnings("unchecked")
    public PagingDozer getUnpaid(@RequestParam(defaultValue = "0") int start,
                                             @RequestParam(defaultValue = "10") int count){
        PageCounter counter = CommonUtil.convertToPageParameter(start, count);
        Page<Order> orderPage = orderService.getUnpaid(counter.getPage(), counter.getCount());
        PagingDozer pagingDozer = new PagingDozer<>(orderPage, OrderSimplifyVO.class);
        pagingDozer.getItems().forEach((o)->{
            ((OrderSimplifyVO) o).setPeriod(this.payTimeLimit);
        });
        return pagingDozer;
    }

    @ScopeLevel
    @GetMapping("/status/{status}")
    @SuppressWarnings("unchecked")
    public PagingDozer getByStatus(@PathVariable Integer status,
                                   @RequestParam(defaultValue = "0") int start,
                                   @RequestParam(defaultValue = "10") int count){
        PageCounter counter = CommonUtil.convertToPageParameter(start, count);
        Page<Order> orderPage = orderService.getByStatus(status, counter.getPage(), counter.getCount());
        PagingDozer pagingDozer = new PagingDozer<>(orderPage, OrderSimplifyVO.class);
        pagingDozer.getItems().forEach((o)->{
            ((OrderSimplifyVO) o).setPeriod(this.payTimeLimit);
        });
        return pagingDozer;
    }

    @ScopeLevel
    @GetMapping("/detail/{id}")
    public OrderPureVO getOrderDetail(@PathVariable Long id){
        Optional<Order> orderOptional = this.orderService.getOrderDetail(id);
        return orderOptional.map((o)->new OrderPureVO(o, payTimeLimit))
                .orElseThrow(()-> new NotFoundException(50009));
    }
}
