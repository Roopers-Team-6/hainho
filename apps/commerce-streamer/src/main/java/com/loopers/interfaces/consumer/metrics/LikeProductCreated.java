package com.loopers.interfaces.consumer.metrics;

public record LikeProductCreated(
        Long userId,
        Long productId
) {
}
