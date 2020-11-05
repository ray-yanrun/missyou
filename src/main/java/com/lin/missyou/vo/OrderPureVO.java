package com.lin.missyou.vo;

import com.lin.missyou.model.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Getter
@Setter
public class OrderPureVO extends Order {

    private int period;
    private Date createTime;

    public OrderPureVO(Order order, int period){
        BeanUtils.copyProperties(order, this);
        this.period = period;
    }
}
