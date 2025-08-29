package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointEventPublisher;
import com.loopers.domain.point.PointUsed;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PointApplicationEventPublisher implements PointEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(PointUsed event) {
        applicationEventPublisher.publishEvent(event);
    }
}
