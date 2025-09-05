package com.loopers.interfaces.consumer.metrics;

public record LikeProductDeleted(
        Long userId,
        Long productId
) {
}
