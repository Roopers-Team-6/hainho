package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductEventPublisher;
import com.loopers.domain.product.ProductFound;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductApplicationEventPublisher implements ProductEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(ProductFound event) {
        applicationEventPublisher.publishEvent(event);
    }
}
