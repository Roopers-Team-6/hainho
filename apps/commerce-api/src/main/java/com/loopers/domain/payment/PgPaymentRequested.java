package com.loopers.domain.payment;

public record PgPaymentRequested(
        Long paymentId,
        String pgTransactionKey,
        String result
) {
    public static PgPaymentRequested of(Long paymentId, String pgTransactionKey, String result) {
        return new PgPaymentRequested(paymentId, pgTransactionKey, result);
    }
}
