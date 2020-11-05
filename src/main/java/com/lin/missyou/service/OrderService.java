package com.lin.missyou.service;

import com.lin.missyou.dto.OrderDTO;
import com.lin.missyou.logic.OrderChecker;
import com.lin.missyou.model.Order;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderService {

    OrderChecker isOk(Long uid, OrderDTO orderDTO);

    Long placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker);

    Page<Order> getUnpaid(Integer page, Integer size);

    Page<Order> getByStatus(Integer status, Integer page, Integer size);

    Optional<Order> getOrderDetail(Long oid);

    void updateOrderPrepayId(Long orderId, String prepayId);
}
