package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeEventPublisher;
import com.loopers.domain.like.LikeProductCreated;
import com.loopers.domain.like.LikeProductDeleted;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeApplicationEventPublisher implements LikeEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(LikeProductCreated event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(LikeProductDeleted event) {
        applicationEventPublisher.publishEvent(event);
    }
}
