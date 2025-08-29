package com.loopers.domain.order;

import java.util.List;

public record OrderCancelled(
        Long orderId,
        List<OrderItem> items

) {
    public static OrderCancelled from(Order order, List<com.loopers.domain.order.OrderItem> orderItems) {
        return new OrderCancelled(
                order.getId(),
                orderItems.stream().map(OrderItem::from).toList()
        );
    }

    public record OrderItem(
            Long productId,
            Long quantity
    ) {
        public static OrderItem from(com.loopers.domain.order.OrderItem item) {
            return new OrderItem(
                    item.getProductId(),
                    item.getQuantity().getValue()
            );
        }
    }
}
