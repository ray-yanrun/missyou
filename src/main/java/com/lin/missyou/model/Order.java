package com.lin.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lin.missyou.core.enumeration.OrderStatus;
import com.lin.missyou.dto.OrderAddressDTO;
import com.lin.missyou.util.CommonUtil;
import com.lin.missyou.util.GenericAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "delete_time is null")
@Table(name="`Order`")  // order是mysql的保留关键字，需要加反引号处理
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalPrice;
    private Integer totalCount;
    private Date expiredTime;
    private Date placedTime;
    private String snapImg;
    private String snapTitle;
    private String snapItems;
    private String snapAddress;
    private String prepayId;
    private BigDecimal finalTotalPrice;
    private Integer status;

    public void setSnapAddress(OrderAddressDTO addressDTO){
        this.snapAddress = GenericAndJson.objectToJson(addressDTO);
    }

    public OrderAddressDTO getSnapAddress(){
        if(this.snapAddress == null){
            return null;
        }
        return GenericAndJson.jsonToObject(this.snapAddress,
                new TypeReference<OrderAddressDTO>(){
                });
    }

    public void setSnapItems(List<OrderSku> skuList){
        if(skuList.isEmpty()){
            return;
        }
        this.snapItems = GenericAndJson.objectToJson(skuList);
    }

    public List<OrderSku> getSnapItems(){
        return GenericAndJson.jsonToObject(this.snapItems,
                new TypeReference<List<OrderSku>>(){
                });
    }

    @JsonIgnore
    public OrderStatus getStatusEnum(){
        return OrderStatus.toType(this.status);
    }

    // 是否可以取消订单
    public Boolean needCancel(){
        if(!this.getStatusEnum().equals(OrderStatus.UNPAID)){
            return true;
        }
        return CommonUtil.isOutOfDate(this.getExpiredTime());
    }

}
