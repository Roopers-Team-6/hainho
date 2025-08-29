package com.loopers.domain.order;

public interface OrderEventPublisher {
    void publish(OrderCreated event);

    void publish(OldPendingOrderFound event);

    void publish(OrderCancelled event);

    void publish(OrderCompleted event);
}
