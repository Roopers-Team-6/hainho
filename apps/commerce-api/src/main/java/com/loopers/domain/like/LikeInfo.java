package com.loopers.domain.like;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LikeInfo {
    public record Get(
            Long count,
            Boolean isLiked
    ) {
        public static Get from(LikeProductCount likeProductCount, Boolean isLiked) {
            return new Get(
                    likeProductCount.getCountValue().getValue(),
                    isLiked
            );
        }
    }
}
