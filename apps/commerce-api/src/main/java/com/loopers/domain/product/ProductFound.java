package com.loopers.domain.product;

public record ProductFound(
        Long productId,
        Long userId
) {
    public static ProductFound of(Long productId, Long userId) {
        return new ProductFound(productId, userId);
    }
}
