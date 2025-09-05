package com.loopers.interfaces.consumer.audit;

import java.util.List;

public record OrderCreated(
        Long orderId,
        Long userId,
        Long couponId,
        List<OrderItem> items,
        Long totalPrice
) {
    public record OrderItem(
            Long productId,
            Long quantity
    ) {
    }
}
