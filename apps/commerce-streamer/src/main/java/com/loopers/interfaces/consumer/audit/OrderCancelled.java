package com.loopers.interfaces.consumer.audit;

import java.util.List;

public record OrderCancelled(
        Long orderId,
        List<OrderItem> items
) {
    public record OrderItem(
            Long productId,
            Long quantity
    ) {
    }
}
