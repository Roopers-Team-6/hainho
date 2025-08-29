package com.loopers.domain.payment;

public record PaymentSucceed(
        Long orderId,
        Long paymentId
) {
    public static PaymentSucceed from(Payment payment) {
        return new PaymentSucceed(payment.getOrderId(), payment.getId());
    }
}
