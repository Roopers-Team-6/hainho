package com.loopers.domain.payment;

public record SucceedPaymentNotFound(
        Long orderId
) {
    public static SucceedPaymentNotFound of(Long orderId) {
        return new SucceedPaymentNotFound(orderId);
    }
}
