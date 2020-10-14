package com.lin.missyou.service;

import com.lin.missyou.model.Coupon;

import java.util.List;

public interface CouponService {

    List<Coupon> getByCategory(Long cid);

    List<Coupon> getWholeStoreCoupons();

    void collectOneCoupon(Long uid, Long couponId);

    List<Coupon> getMyAvailableCoupons(Long uid);

    List<Coupon> getMyUsedCoupons(Long uid);

    List<Coupon> getMyExpiredCoupons(Long uid);

}
