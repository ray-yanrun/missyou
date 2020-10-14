package com.lin.missyou.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "activity_coupon", schema = "sleeve", catalog = "")
public class ActivityCoupon {
    private int id;
    private int couponId;
    private int activityId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "coupon_id")
    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    @Basic
    @Column(name = "activity_id")
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityCoupon that = (ActivityCoupon) o;
        return id == that.id &&
                couponId == that.couponId &&
                activityId == that.activityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, couponId, activityId);
    }
}
