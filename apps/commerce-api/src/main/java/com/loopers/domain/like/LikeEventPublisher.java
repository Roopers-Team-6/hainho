package com.loopers.domain.like;

public interface LikeEventPublisher {
    void publish(LikeProductCreated event);

    void publish(LikeProductDeleted event);
}
