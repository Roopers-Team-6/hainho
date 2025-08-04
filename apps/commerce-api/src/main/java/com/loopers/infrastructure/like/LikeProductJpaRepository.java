package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeProductJpaRepository extends JpaRepository<LikeProduct, Long> {
    boolean existsByUserIdAndProductId(long userId, long productId);

    LikeProduct findByUserIdAndProductId(long userId, long productId);
}
