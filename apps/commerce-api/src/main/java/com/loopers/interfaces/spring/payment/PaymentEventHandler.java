package com.loopers.interfaces.spring.payment;

import com.loopers.domain.payment.CardPaymentCreated;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.PgPaymentRequested;
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
}
