package com.loopers.domain.product;

public interface ProductEventPublisher {
    void publish(ProductFound event);
}
