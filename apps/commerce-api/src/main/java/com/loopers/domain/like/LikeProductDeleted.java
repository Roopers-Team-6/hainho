package com.loopers.domain.like;

public record LikeProductDeleted(
        Long userId,
        Long productId
) {
    public static LikeProductDeleted of(Long userId, Long productId) {
        return new LikeProductDeleted(userId, productId);
    }
}
