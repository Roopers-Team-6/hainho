package com.loopers.domain.payment;

public record CardPaymentCreated(
        Long paymentId,
        Long orderId,
        String cardType,
        String cardNumber,
        Long amount
) {
    public static CardPaymentCreated from(Payment payment, String cardType, String cardNumber) {
        return new CardPaymentCreated(payment.getId(), payment.getOrderId(), cardType, cardNumber, payment.getAmount().getValue());
    }
}
