package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCriteria;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentV1Dto {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PaymentCallback {
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

        public record Response(
                Long orderId
        ) {
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateWithCard {
        public record Request(
                Long orderId,
                String cardType,
                String cardNo
        ) {
            public PaymentCriteria.Create.Card toCriteria(Long userId) {
                return new PaymentCriteria.Create.Card(
                        orderId,
                        cardType,
                        cardNo,
                        userId
                );
            }
        }

        public record Response(
                Long amount
        ) {
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateWithPoint {
        public record Request(
                Long orderId
        ) {
            public PaymentCriteria.Create.Point toCriteria(Long userId) {
                return new PaymentCriteria.Create.Point(
                        orderId,
                        userId
                );
            }
        }

        public record Response(
                Long amount
        ) {
        }
    }
}
