package com.loopers.application.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCriteria {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Create {

        public record Card(
                Long orderId,
                String cardType,
                String cardNo,
                Long userId
        ) {
        }

        public record Point(
                Long orderId,
                Long userId
        ) {
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Callback {
        public record Request(
                String transactionKey,
                String orderId,
                String cardType,
                String cardNo,
                Long amount,
                String status,
                String reason
        ) {

            public PaymentCriteria.Callback.Request toCriteria() {
                return new PaymentCriteria.Callback.Request(
                        transactionKey,
                        orderId,
                        cardType,
                        cardNo,
                        amount,
                        status,
                        reason
                );
            }
        }

        public record Response() {
        }
    }
}
