package com.loopers.domain.payment;

public record PgPaymentCompleted(
        String orderId,
        String transactionKey,
        String status
) {
    public static PgPaymentCompleted of(String orderId, String transactionKey, String status) {
        return new PgPaymentCompleted(orderId, transactionKey, status);
    }
}
