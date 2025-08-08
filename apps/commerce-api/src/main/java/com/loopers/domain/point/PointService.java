package com.loopers.domain.point;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public Long usePoint(Long userId, Long amount) {
        Point point = getPointWithLock(userId);
        point.use(amount);
        return point.getBalance().getValue();
    }

    @Transactional
    public Long chargePoint(Long userId, Long amount) {
        Point point = getPointWithLock(userId);
        point.charge(amount);
        return point.getBalance().getValue();
    }

    @Transactional
    public void createPoint(Long userId) {
        if (pointRepository.existsByUserId(userId)) {
            return;
        }
        Point point = Point.create(userId);
        pointRepository.save(point);
    }

    public Long getPoint(Long userId) {
        Point point = getPointByUserId(userId);
        return point.getBalance().getValue();
    }

    private Point getPointWithLock(Long userId) {
        return pointRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저의 포인트 정보를 찾을 수 없습니다. userId: " + userId));
    }

    private Point getPointByUserId(Long userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저의 포인트 정보를 찾을 수 없습니다. userId: " + userId));
    }
}
