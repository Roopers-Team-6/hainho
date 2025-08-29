package com.loopers.domain.point;

public interface PointEventPublisher {
    void publish(PointUsed event);
}
