package com.lin.missyou.logic;

import com.lin.missyou.bo.SkuOrderBO;
import com.lin.missyou.core.enumeration.CouponType;
import com.lin.missyou.core.money.MoneyDiscount;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Category;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.UserCoupon;
import com.lin.missyou.util.CommonUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CouponChecker {

    private Coupon coupon;
    private MoneyDiscount moneyDiscount;

    public CouponChecker(Coupon coupon, MoneyDiscount moneyDiscount){
        this.coupon = coupon;
        this.moneyDiscount = moneyDiscount;
    }

    public void isOk(){
        Boolean isInTimeLine = CommonUtil.isInTimeLine(new Date(), this.coupon.getStartTime(), this.coupon.getEndTime());
        if(!isInTimeLine){
            throw new ForbiddenException(40007);
        }
    }

    public void finalTotalPriceIsOk(BigDecimal orderFinalTotalPrice, BigDecimal serverTotalPrice){
        BigDecimal serverFinalTotalPrice;
        switch (CouponType.toType(this.coupon.getType())) {
            case FULL_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(this.coupon.getMinus());
                break;
            case FULL_OFF:
                serverFinalTotalPrice = this.moneyDiscount.discount(serverTotalPrice, this.coupon.getRate());
                break;
            case NO_THRESHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(this.coupon.getMinus());
                if(serverFinalTotalPrice.compareTo(new BigDecimal("0")) <= 0){
                    throw new ForbiddenException(50008);
                }
                break;
            default:
                throw new ForbiddenException(40009);
        }
        if(serverFinalTotalPrice.compareTo(orderFinalTotalPrice) != 0){
            throw new ForbiddenException(50008);
        }
    }

    // 校验优惠券是否有品类限制
    public void canBeUsed(List<SkuOrderBO> skuOrderBOList, BigDecimal serverTotalPrice){
        BigDecimal orderCategoryPrice;  // 本优惠券所属品类的订单单品总价格
        if(this.coupon.getWholeStore()){  // 若优惠券是全场券
            orderCategoryPrice = serverTotalPrice;
        } else {
            List<Long> categoryList = this.coupon.getCategoryList().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            orderCategoryPrice = this.getPriceByCategoryList(skuOrderBOList, categoryList);
        }
        this.couponCanBeUsed(orderCategoryPrice);
    }

    private void couponCanBeUsed(BigDecimal orderCategoryPrice){
        switch (CouponType.toType(this.coupon.getType())){
            case FULL_OFF:
            case FULL_MINUS:
                int compare = orderCategoryPrice.compareTo(this.coupon.getFullMoney());
                if(compare > 0){
                    throw new ParameterException(40008);
                }
                break;
            case NO_THRESHOLD_MINUS:
                break;
            default:
                throw new ParameterException(40009);
        }
    }

    // 求若干个分类下所有商品的总价格
    private BigDecimal getPriceByCategoryList(List<SkuOrderBO> skuOrderBOList, List<Long> cidList){
        return cidList.stream()
                .map(cid -> this.getPriceByCategory(skuOrderBOList, cid))
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal("0"));
    }

    // 求一个分类下所有商品的总价格
    private BigDecimal getPriceByCategory(List<SkuOrderBO> skuOrderBOList, Long cid){
        return skuOrderBOList.stream()
                .filter(sku -> sku.getCategoryId().equals(cid))
                .map(SkuOrderBO::getTotalPrice)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal("0"));
    }
}
