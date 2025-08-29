package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentApplicationEventPublisher implements PaymentEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(CardPaymentCreated event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(PgPaymentRequested event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(PaymentFailed event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(PgPaymentCompleted event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(PaymentSucceed event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(SucceedPaymentNotFound event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(PaymentSucceedDuplicated event) {
        applicationEventPublisher.publishEvent(event);
    }
}
