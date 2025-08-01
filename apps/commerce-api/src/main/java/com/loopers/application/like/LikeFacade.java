package com.loopers.application.like;

import com.loopers.domain.like.LikeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeFacade {
    private final LikeService likeService;

    public void likeProduct(Long userId, Long productId) {
        Boolean isSuccess = likeService.likeProduct(userId, productId);
        if (isSuccess) {
            likeService.increaseLikeProductCount(productId);
        }
    }

    public void cancelLikeProduct(Long userId, Long productId) {
        Boolean isSuccess = likeService.likeProductCancel(userId, productId);
        if (isSuccess) {
            likeService.decreaseLikeProductCount(productId);
        }
    }
}
