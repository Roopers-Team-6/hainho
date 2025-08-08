package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeProductCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeProductCountJpaRepository extends JpaRepository<LikeProductCount, Long> {
    Optional<LikeProductCount> findByProductId(Long productId);

    @Modifying
    @Query("UPDATE LikeProductCount l SET l.countValue.value = l.countValue.value + 1 WHERE l.productId = :productId")
    Integer increaseLikeCount(Long productId);

    @Modifying
    @Query("UPDATE LikeProductCount l SET l.countValue.value = l.countValue.value - 1 WHERE l.productId = :productId")
    Integer decreaseLikeCount(Long productId);
}