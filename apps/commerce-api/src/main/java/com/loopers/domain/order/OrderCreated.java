package com.loopers.domain.order;

import java.util.List;

public record OrderCreated(
        Long orderId,
        Long userId,
        Long couponId,
        List<OrderItem> items,
        Long totalPrice
) {
    public static OrderCreated from(Order order, List<com.loopers.domain.order.OrderItem> items, Long couponId) {
        return new OrderCreated(
                order.getId(),
                order.getUserId(),
                couponId,
                items.stream().map(OrderItem::from).toList(),
                order.getTotalPrice().getValue()
        );
    }

    public record OrderItem(
            Long productId,
            Long quantity
    ) {
        public static OrderItem from(com.loopers.domain.order.OrderItem orderItem) {
            return new OrderItem(orderItem.getProductId(), orderItem.getQuantity().getValue());
        }
    }

}
