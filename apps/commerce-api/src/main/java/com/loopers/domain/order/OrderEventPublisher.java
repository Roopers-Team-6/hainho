package com.loopers.domain.order;

public interface OrderEventPublisher {
    void publish(OrderCreated event);
}
