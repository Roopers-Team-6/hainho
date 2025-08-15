package com.loopers.domain.point;

public class PointFixture {
    private static final Long VALID_USER_ID = 1L;

    public static Point createPoint(Long userId) {
        return Point.create(userId);
    }

    public static Point createPoint() {
        return createPoint(VALID_USER_ID);
    }

    public static Point createPointWithUserId(Long userId) {
        return createPoint(userId);
    }
}
