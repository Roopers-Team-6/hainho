package com.loopers.domain.point;

public record PointUsed(
        Long paymentId,
        Long orderId
) {
    public static PointUsed of(Long paymentId, Long orderId) {
        return new PointUsed(paymentId, orderId);
    }
}
