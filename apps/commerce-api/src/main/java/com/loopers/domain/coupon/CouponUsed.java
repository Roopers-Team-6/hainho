package com.loopers.domain.coupon;

public record CouponUsed(
        Long couponId,
        Long orderId,
        Long discountAmount
) {
    public static CouponUsed from(CouponIssuance couponIssuance, Long discountAmount) {
        return new CouponUsed(
                couponIssuance.getCouponId(),
                couponIssuance.getUsedOrderId(),
                discountAmount
        );
    }
}
