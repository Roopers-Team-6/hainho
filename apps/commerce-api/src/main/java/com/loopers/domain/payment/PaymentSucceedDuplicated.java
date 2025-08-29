package com.loopers.domain.payment;

public record PaymentSucceedDuplicated(
        Long orderId
) {
    public static PaymentSucceedDuplicated of(Long orderId) {
        return new PaymentSucceedDuplicated(orderId);
    }
}
