package com.loopers.domain.payment;

public interface PaymentEventPublisher {
    void publish(CardPaymentCreated event);

    void publish(PgPaymentRequested event);

    void publish(PgPaymentFailed event);
}
