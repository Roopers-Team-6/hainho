package com.loopers.domain.coupon;

import java.math.BigDecimal;

public class RateDiscountCouponFixture {
    private static final String VALID_NAME = "couponName";
    private static final BigDecimal VALID_DISCOUNT_RATE = BigDecimal.valueOf(0.1); // 10%

    public static RateDiscountCoupon createRateDiscountCoupon(BigDecimal discountRate, String name) {
        return RateDiscountCoupon.create(discountRate, name);
    }

    public static RateDiscountCoupon createRateDiscountCoupon() {
        return createRateDiscountCoupon(VALID_DISCOUNT_RATE, VALID_NAME);
    }

    public static RateDiscountCoupon createRateDiscountCouponWithDiscountRate(BigDecimal discountRate) {
        return createRateDiscountCoupon(discountRate, VALID_NAME);
    }

    public static RateDiscountCoupon createRateDiscountCouponWithName(String name) {
        return createRateDiscountCoupon(VALID_DISCOUNT_RATE, name);
    }
}
