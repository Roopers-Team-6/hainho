package com.loopers.domain.coupon;

public interface CouponEventPublisher {
    void publish(CouponUsed event);
}
