package com.loopers.domain.payment;

public interface PaymentEventPublisher {
    void publish(CardPaymentCreated event);

    void publish(PgPaymentRequested event);

    void publish(PaymentFailed event);

    void publish(PgPaymentCompleted events);

    void publish(PaymentSucceed events);

    void publish(SucceedPaymentNotFound event);

    void publish(PaymentSucceedDuplicated event);
}
