package com.loopers.infrastructure.order;

import com.loopers.domain.order.OldPendingOrderFound;
import com.loopers.domain.order.OrderCancelled;
import com.loopers.domain.order.OrderCreated;
import com.loopers.domain.order.OrderEventPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderApplicationEventPublisher implements OrderEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(OrderCreated event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(OldPendingOrderFound event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(OrderCancelled event) {
        applicationEventPublisher.publishEvent(event);
    }

}
