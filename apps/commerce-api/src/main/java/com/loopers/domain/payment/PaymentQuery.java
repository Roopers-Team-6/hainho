package com.loopers.domain.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentQuery {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Card {
        public record Payment(
                String transactionKey
        ) {
        }

        public record Order(
                Long orderId
        ) {
        }
    }
}
