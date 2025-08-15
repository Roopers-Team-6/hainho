package com.loopers.domain.coupon;

public class CouponIssuanceFixture {
    private static final Long VALID_COUPON_ID = 1L;
    private static final Long VALID_USER_ID = 2L;

    public static CouponIssuance createCouponIssuance(Long couponId, Long userId) {
        return CouponIssuance.create(couponId, userId);
    }

    public static CouponIssuance createCouponIssuance() {
        return createCouponIssuance(VALID_COUPON_ID, VALID_USER_ID);
    }

    public static CouponIssuance createCouponIssuanceWithUserId(Long userId) {
        return createCouponIssuance(VALID_COUPON_ID, userId);
    }

    public static CouponIssuance createCouponIssuanceWithCouponId(Long couponId) {
        return createCouponIssuance(couponId, VALID_USER_ID);
    }
}
