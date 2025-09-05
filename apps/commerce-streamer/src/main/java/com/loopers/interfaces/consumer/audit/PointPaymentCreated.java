package com.loopers.interfaces.consumer.audit;

public record PointPaymentCreated(
        Long userId,
        Long paymentId,
        Long orderId,
        Long amount
) {
}
