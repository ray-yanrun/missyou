package com.lin.missyou.service;

import com.lin.missyou.core.enumeration.CouponStatus;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.Activity;
import com.lin.missyou.model.Coupon;
import com.lin.missyou.model.UserCoupon;
import com.lin.missyou.repository.ActivityRepository;
import com.lin.missyou.repository.CouponRepository;
import com.lin.missyou.repository.UserCouponRepository;
import com.lin.missyou.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    @Override
    public List<Coupon> getByCategory(Long cid) {
        return couponRepository.findByCategory(cid, new Date());
    }

    @Override
    public List<Coupon> getWholeStoreCoupons() {
        return couponRepository.findByWholeStore(true, new Date());
    }

    @Override
    public void collectOneCoupon(Long uid, Long couponId) {
        // 校验优惠券是否存在
        this.couponRepository.findById(couponId)
                .orElseThrow(()-> new NotFoundException(40003));

        Activity activity = this.activityRepository.findByCouponListId(couponId)
                .orElseThrow(()-> new NotFoundException(40010));
        // 检验优惠券对应的活动是否已经过期
        Date now = new Date();
        Boolean isIn = CommonUtil.isInTimeLine(now, activity.getStartTime(), activity.getEndTime());
        if(!isIn){
            throw new ParameterException(40005);
        }
        // 校验是否已经领取过该优惠券
        this.userCouponRepository.findFirstByUserIdAndCouponId(uid, couponId)
                .orElseThrow(()->new ParameterException(40006));

        // 领取优惠券即入库到user_coupon表中
        UserCoupon userCouponNew = UserCoupon.builder()
                .userId(uid)
                .couponId(couponId)
                .status(CouponStatus.AVAILABLE.getValue())
                .createTime(now)
                .build();
        userCouponRepository.save(userCouponNew);
    }

    @Override
    public List<Coupon> getMyAvailableCoupons(Long uid) {
        return couponRepository.findMyAvailable(uid, new Date());
    }

    @Override
    public List<Coupon> getMyUsedCoupons(Long uid) {
        return couponRepository.findMyUsed(uid, new Date());
    }

    @Override
    public List<Coupon> getMyExpiredCoupons(Long uid) {
        return couponRepository.findMyExpired(uid, new Date());
    }
}
