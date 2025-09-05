package com.loopers.interfaces.consumer.audit;

import java.util.List;

public record OrderCompleted(
        Long orderId,
        Long userId,
        Long totalPrice,
        List<OrderItem> items
) {
    public record OrderItem(
            Long productId,
            Long quantity,
            Long price
    ) {
    }
}
