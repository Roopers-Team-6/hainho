package com.loopers.interfaces.spring.log;

import com.loopers.domain.like.LikeProductCreated;
import com.loopers.domain.like.LikeProductDeleted;
import com.loopers.domain.order.OrderCreated;
import com.loopers.domain.product.ProductFound;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class LogEventHandler {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreated event) {
        log.info("유저 {}가 주문 {}를 생성했습니다.", event.userId(), event.orderId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LikeProductCreated event) {
        log.info("유저 {}가 상품 {}를 좋아요했습니다.", event.userId(), event.productId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LikeProductDeleted event) {
        log.info("유저 {}가 상품 {}의 좋아요를 취소했습니다.", event.userId(), event.productId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductFound event) {
        log.info("유저 {}가 상품 {}를 조회했습니다.", event.userId(), event.productId());
    }
}
