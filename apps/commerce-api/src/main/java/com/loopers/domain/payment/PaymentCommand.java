package com.loopers.domain.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCommand {
    public record Create(
            Long userId,
            Long orderId,
            String paymentMethod,
            Long amount,
            String cardType,
            String cardNumber
    ) {
    }

    public record MarkResult(
            Long paymentId,
            String transactionKey,
            String resultStatus
    ) {
    }

    public record MarkFinalResult(
            String transactionKey,
            String resultStatus
    ) {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Card {
        public record Payment(
                Long orderId,
                String cardType,
                String cardNumber,
                Long amount
        ) {
        }
    }
}
