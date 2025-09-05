package com.loopers.interfaces.consumer.audit;

public record PaymentFailed(
        Long orderId,
        Long paymentId
) {
}
