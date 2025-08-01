package com.loopers.domain.order;

public class OrderFixture {
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_TOTAL_PRICE = 10000L;

    public static Order createOrder(Long userId, Long totalPrice) {
        return Order.create(userId, totalPrice);
    }

    public static Order createOrder() {
        return createOrder(VALID_USER_ID, VALID_TOTAL_PRICE);
    }

    public static Order createOrderWithUserId(Long userId) {
        return createOrder(userId, VALID_TOTAL_PRICE);
    }

    public static Order createOrderWithTotalPrice(Long totalPrice) {
        return createOrder(VALID_USER_ID, totalPrice);
    }
}
