package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeProductCount;
import com.loopers.domain.like.LikeProductCountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeProductCountRepositoryImpl implements LikeProductCountRepository {
    private final LikeProductCountJpaRepository likeProductCountJpaRepository;

    @Override
    public LikeProductCount save(LikeProductCount likeProductCount) {
        return likeProductCountJpaRepository.save(likeProductCount);
    }

    @Override
    public Optional<LikeProductCount> find(Long productId) {
        return likeProductCountJpaRepository.findByProductId(productId);
    }
}
