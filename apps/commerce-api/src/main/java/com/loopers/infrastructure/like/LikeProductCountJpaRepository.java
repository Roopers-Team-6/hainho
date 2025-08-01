package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeProductCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeProductCountJpaRepository extends JpaRepository<LikeProductCount, Long> {
    Optional<LikeProductCount> findByProductId(Long productId);
}
