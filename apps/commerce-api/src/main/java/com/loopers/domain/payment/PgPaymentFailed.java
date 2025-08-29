package com.loopers.domain.payment;

public record PgPaymentFailed(
        Long orderId,
        Long paymentId
) {
    public static PgPaymentFailed of(Long orderId, Long paymentId) {
        return new PgPaymentFailed(orderId, paymentId);
    }
}
