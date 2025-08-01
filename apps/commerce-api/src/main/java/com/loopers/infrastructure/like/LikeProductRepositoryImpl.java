package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeProduct;
import com.loopers.domain.like.LikeProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeProductRepositoryImpl implements LikeProductRepository {
    private final LikeProductJpaRepository likeProductJpaRepository;

    @Override
    public LikeProduct save(LikeProduct likeProduct) {
        return likeProductJpaRepository.save(likeProduct);
    }

    @Override
    public void delete(LikeProduct likeProduct) {
        likeProductJpaRepository.delete(likeProduct);
    }

    @Override
    public boolean exists(long userId, long productId) {
        return likeProductJpaRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public Optional<LikeProduct> find(long userId, long productId) {
        return Optional.ofNullable(likeProductJpaRepository.findByUserIdAndProductId(userId, productId));
    }
}
