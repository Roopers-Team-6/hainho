package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentInfo;

import java.util.List;

public class PgSimulatorDto {
    public class CardPayment {
        public class Create {
            public record Request(
                    String orderId,
                    String cardType,
                    String cardNo,
                    Long amount,
                    String callbackUrl
            ) {
                public static Request from(
                        PaymentCommand.Card.Payment command,
                        String callbackUrl
                ) {
                    return new Request(
                            orderIdFromCommand(command),
                            command.cardType(),
                            command.cardNumber(),
                            command.amount(),
                            callbackUrl
                    );
                }

                private static String orderIdFromCommand(PaymentCommand.Card.Payment command) {
                    if (command.orderId() < 100000L) {
                        return String.format("%06d", command.orderId());
                    }
                    return command.orderId().toString();
                }
            }

            public record Response(
                    String transactionKey,
                    String status,
                    String reason
            ) {
                public PaymentInfo.Card.Result toPaymentInfo() {
                    return new PaymentInfo.Card.Result(
                            transactionKey,
                            status,
                            reason
                    );
                }
            }
        }

        public class Query {
//            val transactionKey: String,
//            val orderId: String,
//            val cardType: CardTypeDto,
//            val cardNo: String,
//            val amount: Long,
//            val status: TransactionStatusResponse,
//            val reason: String?,

            public record Response(
                    String transactionKey,
                    String orderId,
                    String cardType,
                    String cardNo,
                    Long amount,
                    String status,
                    String reason
            ) {
                public PaymentInfo.Card.Get toPaymentInfo() {
                    return new PaymentInfo.Card.Get(
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
        }
    }

    public class CardOrder {
        public class Query {
            public record Response(
                    List<PgSimulatorDto.CardPayment.Query.Response> cardPayments
            ) {
                public PaymentInfo.Card.Order toPaymentInfo() {
                    List<PaymentInfo.Card.Get> gets = cardPayments.stream()
                            .map(PgSimulatorDto.CardPayment.Query.Response::toPaymentInfo)
                            .toList();
                    return new PaymentInfo.Card.Order(gets);
                }
            }
        }
    }
}
