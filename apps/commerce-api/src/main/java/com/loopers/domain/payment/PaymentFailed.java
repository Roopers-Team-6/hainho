package com.loopers.domain.payment;

public record PaymentFailed(
        Long orderId,
        Long paymentId
) {
    public static PaymentFailed of(Long orderId, Long paymentId) {
        return new PaymentFailed(orderId, paymentId);
    }
}
