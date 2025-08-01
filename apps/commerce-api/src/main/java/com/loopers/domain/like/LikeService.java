package com.loopers.domain.like;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class LikeService {
    private final LikeProductRepository likeProductRepository;
    private final LikeProductCountRepository likeProductCountRepository;

    @Transactional
    public Boolean likeProduct(Long userId, Long productId) {
        if (likeProductRepository.exists(userId, productId)) {
            return false;
        }
        LikeProduct likeProduct = LikeProduct.create(userId, productId);
        likeProductRepository.save(likeProduct);
        return true;
    }

    @Transactional
    public Boolean likeProductCancel(Long userId, Long productId) {
        Optional<LikeProduct> optionalLikeProduct = likeProductRepository.find(userId, productId);
        if (optionalLikeProduct.isEmpty()) {
            return false;
        }
        LikeProduct likeProduct = optionalLikeProduct.get();
        likeProductRepository.delete(likeProduct);
        return true;
    }

    @Transactional
    public void increaseLikeProductCount(Long productId) {
        Optional<LikeProductCount> optional = findLikeProductCount(productId);
        if (optional.isEmpty()) {
            return;
        }
        LikeProductCount likeProductCount = optional.get();
        likeProductCount.increase();
    }

    @Transactional
    public void decreaseLikeProductCount(Long productId) {
        Optional<LikeProductCount> optional = findLikeProductCount(productId);
        if (optional.isEmpty()) {
            return;
        }
        LikeProductCount likeProductCount = optional.get();
        likeProductCount.decrease();
    }

    private Optional<LikeProductCount> findLikeProductCount(Long productId) {
        Optional<LikeProductCount> optional = likeProductCountRepository.find(productId);
        if (optional.isEmpty()) {
            log.error("Like product count not found for productId: {}", productId);
        }
        return optional;
    }

    @Transactional(readOnly = true)
    public LikeInfo.Get getLikeProductInfo(Long userId, Long productId) {
        LikeProductCount likeProductCount = likeProductCountRepository.find(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품 좋아요 카운트 데이터가 존재하지 않습니다. productId: " + productId));
        Boolean isLiked = likeProductRepository.exists(userId, productId);
        return LikeInfo.Get.from(likeProductCount, isLiked);
    }
}
