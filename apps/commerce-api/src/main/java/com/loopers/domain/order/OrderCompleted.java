package com.loopers.domain.order;

import java.util.List;

public record OrderCompleted(
        Long orderId,
        Long userId,
        Long totalPrice,
        List<OrderItem> items
) {
    public static OrderCompleted from(Order order, List<com.loopers.domain.order.OrderItem> items) {
        return new OrderCompleted(
                order.getId(),
                order.getUserId(),
                order.getTotalPrice().getValue(),
                items.stream().map(OrderItem::from).toList()
        );
    }

    public record OrderItem(
            Long productId,
            Long quantity,
            Long price
    ) {
        public static OrderItem from(com.loopers.domain.order.OrderItem orderItem) {
            return new OrderItem(
                    orderItem.getProductId(),
                    orderItem.getQuantity().getValue(),
                    orderItem.getPrice().getValue()
            );
        }
    }
}
