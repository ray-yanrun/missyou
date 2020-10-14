package com.lin.missyou.repository;

import com.lin.missyou.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("select c from Coupon c\n" +
            "join c.categoryList ca \n" +
            "join Activity a on a.id = c.activityId\n" +
            "where ca.id = :cid and a.startTime < :now and a.endTime > :now")
    List<Coupon> findByCategory(Long cid, Date now);

    @Query("select c from Coupon c\n" +
            "join Activity a on a.id = c.activityId\n" +
            "where c.wholeStore = :isWholeStore and a.startTime < :now and a.endTime > :now")
    List<Coupon> findByWholeStore(Boolean isWholeStore, Date now);

    Optional<Coupon> findById(Long id);

    @Query("select c from Coupon c \n" +
            "join UserCoupon uc on c.id = uc.couponId\n" +
            "join User u on u.id = uc.userId\n" +
            "where uc.status = 1 \n" +
            "and u.id = :uid \n" +
            "and c.startTime < :now and c.endTime > :now\n" +
            "and uc.orderId is null")
    List<Coupon> findMyAvailable(Long uid, Date now);

    @Query("select c from Coupon c\n" +
            "join UserCoupon uc on c.id = uc.couponId\n" +
            "join User u on u.id = uc.userId\n" +
            "where uc.status = 2 and uc.orderId is not null\n" +
            "and c.startTime < :now and c.endTime > :now\n" +
            "and u.id = :uid")
    List<Coupon> findMyUsed(Long uid, Date now);

    @Query("select c from Coupon c\n" +
            "join UserCoupon uc on c.id = uc.couponId\n" +
            "join User u on u.id = uc.userId\n" +
            "where uc.status <> 2 and uc.orderId is null\n" +
            "and c.endTime < :now\n" +
            "and u.id = :uid")
    List<Coupon> findMyExpired(Long uid, Date now);
}
