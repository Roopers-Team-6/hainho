package com.loopers.domain.order;

public class OrderItemFixture {
    private static final Long VALID_ORDER_ID = 1L;
    private static final Long VALID_PRODUCT_ID = 2L;
    private static final Long VALID_QUANTITY = 3L;
    private static final Long VALID_PRICE = 1000L;

    public static OrderItem createOrderItem(Long orderId, Long productId, Long quantity, Long price) {
        return OrderItem.create(orderId, productId, quantity, price);
    }

    public static OrderItem createOrderItem() {
        return createOrderItem(VALID_ORDER_ID, VALID_PRODUCT_ID, VALID_QUANTITY, VALID_PRICE);
    }

    public static OrderItem createOrderItemWithOrderId(Long orderId) {
        return createOrderItem(orderId, VALID_PRODUCT_ID, VALID_QUANTITY, VALID_PRICE);
    }

    public static OrderItem createOrderItemWithProductId(Long productId) {
        return createOrderItem(VALID_ORDER_ID, productId, VALID_QUANTITY, VALID_PRICE);
    }

    public static OrderItem createOrderItemWithQuantity(Long quantity) {
        return createOrderItem(VALID_ORDER_ID, VALID_PRODUCT_ID, quantity, VALID_PRICE);
    }

    public static OrderItem createOrderItemWithPrice(Long price) {
        return createOrderItem(VALID_ORDER_ID, VALID_PRODUCT_ID, VALID_QUANTITY, price);
    }
}
