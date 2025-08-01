package com.loopers.domain.like;

import java.util.Optional;

public interface LikeProductRepository {
    LikeProduct save(LikeProduct likeProduct);

    void delete(LikeProduct likeProduct);

    boolean exists(long userId, long productId);

    Optional<LikeProduct> find(long userId, long productId);
}
