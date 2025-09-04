package com.loopers.interfaces.spring.payment;

import com.loopers.domain.order.OldPendingOrderFound;
import com.loopers.domain.payment.*;
import com.loopers.domain.point.PointUsed;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEventHandler {
    private final PaymentService paymentService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CardPaymentCreated event) {
        PaymentCommand.Card.Payment command = new PaymentCommand.Card.Payment(
                event.orderId(),
                event.cardType(),
                event.cardNumber(),
                event.amount()
        );
        paymentService.requestCardPayment(command);
    }

    @Async
    @EventListener
    public void handle(PgPaymentRequested event) {
        PaymentCommand.MarkResult command = new PaymentCommand.MarkResult(
                event.paymentId(),
                event.pgTransactionKey(),
                event.result()
        );
        paymentService.markResult(command);
    }

    @Async
    @EventListener
    public void handle(PgPaymentCompleted event) {
        PaymentCommand.MarkFinalResult command = new PaymentCommand.MarkFinalResult(
                event.transactionKey(),
                event.status()
        );
        paymentService.markFinalResult(command);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OldPendingOrderFound event) {
        paymentService.verifyOrderPaymentResult(event.orderId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentSucceedDuplicated event) {
        // 중복 결제 취소
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(PointUsed event) {
        paymentService.completePointPayment(event.orderId(), event.paymentId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleAfterRollback(PointUsed event) {
        paymentService.failPointPayment(event.orderId(), event.paymentId());
    }
}
