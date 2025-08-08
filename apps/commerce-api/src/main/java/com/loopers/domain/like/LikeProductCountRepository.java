package com.loopers.domain.like;

import java.util.Optional;

public interface LikeProductCountRepository {
    LikeProductCount save(LikeProductCount likeProductCount);

    Optional<LikeProductCount> find(Long productId);

    Integer increaseLikeCount(Long productId);

    Integer decreaseLikeCount(Long productId);
}
