package com.loopers.domain.payment;

public record PointPaymentCreated(
        Long userId,
        Long paymentId,
        Long orderId,
        Long amount
) {
    public static PointPaymentCreated from(Payment payment, Long userId) {
        return new PointPaymentCreated(
                userId,
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount().getValue()
        );
    }
}
