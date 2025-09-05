package com.loopers.interfaces.consumer.audit;

public record CardPaymentCreated(
        Long paymentId,
        Long orderId,
        String cardType,
        String cardNumber,
        Long amount
) {
}
