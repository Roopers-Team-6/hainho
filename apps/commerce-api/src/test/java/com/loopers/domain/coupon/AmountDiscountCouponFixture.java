package com.loopers.domain.coupon;

public class AmountDiscountCouponFixture {
    private static final String VALID_NAME = "couponName";
    private static final Long VALID_DISCOUNT_AMOUNT = 1000L;

    public static AmountDiscountCoupon createAmountDiscountCoupon(Long discountAmount, String name) {
        return AmountDiscountCoupon.create(discountAmount, name);
    }

    public static AmountDiscountCoupon createAmountDiscountCoupon() {
        return createAmountDiscountCoupon(VALID_DISCOUNT_AMOUNT, VALID_NAME);
    }

    public static AmountDiscountCoupon createAmountDiscountCouponWithDiscountAmount(Long discountAmount) {
        return createAmountDiscountCoupon(discountAmount, VALID_NAME);
    }

    public static AmountDiscountCoupon createAmountDiscountCouponWithName(String name) {
        return createAmountDiscountCoupon(VALID_DISCOUNT_AMOUNT, name);
    }
}
