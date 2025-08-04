package com.loopers.domain.order;

public record OrderItemCommand(
        Long productId,
        Long quantity,
        Long price
) {
}
