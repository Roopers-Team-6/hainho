package com.loopers.interfaces.spring.order;

import com.loopers.domain.coupon.CouponUsed;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentFailed;
import com.loopers.domain.payment.PaymentSucceed;
import com.loopers.domain.payment.SucceedPaymentNotFound;
import com.loopers.domain.point.PointUsed;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEventHandler {
    private final OrderService orderService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(CouponUsed event) {
        orderService.applyDiscount(event.orderId(), event.discountAmount());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentFailed event) {
        orderService.markPending(event.orderId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentSucceed event) {
        orderService.markCompleted(event.orderId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SucceedPaymentNotFound event) {
        orderService.markCancelled(event.orderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(PointUsed event) {
        orderService.markCompleted(event.orderId());
    }
}
