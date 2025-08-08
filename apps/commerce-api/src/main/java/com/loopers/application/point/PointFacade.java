package com.loopers.application.point;

import com.loopers.domain.point.PointService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PointFacade {

    private final PointService pointService;

    public Long getPoint(Long userId) {
        return pointService.getPoint(userId);
    }

    public Long chargePoint(Long userId, Long amount) {
        return pointService.chargePoint(userId, amount);
    }
}
