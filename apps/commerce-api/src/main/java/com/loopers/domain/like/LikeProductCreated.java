package com.loopers.domain.like;

public record LikeProductCreated(
        Long userId,
        Long productId
) {
    public static LikeProductCreated of(Long userId, Long productId) {
        return new LikeProductCreated(userId, productId);
    }
}
