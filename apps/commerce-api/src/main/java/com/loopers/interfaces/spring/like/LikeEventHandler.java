package com.loopers.interfaces.spring.like;

import com.loopers.domain.like.LikeProductCreated;
import com.loopers.domain.like.LikeProductDeleted;
import com.loopers.domain.like.LikeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeEventHandler {
    private final LikeService likeService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeProductCreated(LikeProductCreated event) {
        likeService.increaseLikeProductCount(event.productId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeProductDeleted(LikeProductDeleted event) {
        likeService.decreaseLikeProductCount(event.productId());
    }
}
