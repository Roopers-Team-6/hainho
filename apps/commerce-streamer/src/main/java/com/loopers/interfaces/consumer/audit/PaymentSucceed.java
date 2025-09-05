package com.loopers.interfaces.consumer.audit;

public record PaymentSucceed(
        Long orderId,
        Long paymentId
) {
}
