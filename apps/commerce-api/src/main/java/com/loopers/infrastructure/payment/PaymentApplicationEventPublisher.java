package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.CardPaymentCreated;
import com.loopers.domain.payment.PaymentEventPublisher;
import com.loopers.domain.payment.PgPaymentFailed;
import com.loopers.domain.payment.PgPaymentRequested;
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
    public void publish(PgPaymentFailed event) {
        applicationEventPublisher.publishEvent(event);
    }
}
