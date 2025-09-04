package com.loopers.domain.payment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PaymentTest {
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_ORDER_ID = 2L;
    private static final String VALID_PAYMENT_METHOD = PaymentMethod.CARD.name();
    private static final Long VALID_AMOUNT = 1000L;
    private static final String VALID_CARD_TYPE = "VISA";
    private static final String VALID_CARD_NO = "1234-5678-9012-3456";

    @Nested
    @DisplayName("Payment를 생성할 때,")
    class CreatePayment {
        @Test
        @DisplayName("OrderId가 null이면, IllegalArgumentException 예외를 던진다.")
        void createPaymentWithNullOrderId() {
            // arrange
            PaymentCommand.Create command = new PaymentCommand.Create(VALID_USER_ID, null, VALID_PAYMENT_METHOD, VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO);

            // act && assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> Payment.create(command));
        }

        @Test
        @DisplayName("PaymentMethod가 null이면, IllegalArgumentException 예외를 던진다.")
        void createPaymentWithNullPaymentMethod() {
            // arrange
            PaymentCommand.Create command = new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, null, VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO);

            // act && assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> Payment.create(command));
        }

        @Test
        @DisplayName("PaymentMethod가 유효한 값이 아니면, IllegalArgumentException 예외를 던진다.")
        void createPaymentWithInvalidPaymentMethod() {
            // arrange
            PaymentCommand.Create command = new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, "invalidPaymentMethod", VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO);

            // act && assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> Payment.create(command));
        }

        @Test
        @DisplayName("Amount가 null이면, IllegalArgumentException 예외를 던진다.")
        void createPaymentWithNullAmount() {
            // arrange
            PaymentCommand.Create command = new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, VALID_PAYMENT_METHOD, null, VALID_CARD_TYPE, VALID_CARD_NO);

            // act && assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> Payment.create(command));
        }

        @Test
        @DisplayName("Amount가 0 이하이면, IllegalArgumentException 예외를 던진다.")
        void createPaymentWithZeroOrNegativeAmount() {
            // arrange
            PaymentCommand.Create command = new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, VALID_PAYMENT_METHOD, 0L, VALID_CARD_TYPE, VALID_CARD_NO);

            // act && assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> Payment.create(command));
        }
    }

    @Nested
    @DisplayName("Payment를 완료할 때,")
    class CompletePayment {
        @Test
        @DisplayName("트랜잭션 키가 null이면, IllegalArgumentException 예외를 던진다.")
        void completePaymentWithNullTransactionKey() {
            // arrange
            Payment payment = Payment.create(new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, VALID_PAYMENT_METHOD, VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO));
            payment.markRequested("transactionKey");

            // act && assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> payment.markCompleted(null));
        }

        @Test
        @DisplayName("결제 상태가 완료 가능한 상태가 아니면, IllegalStateException 예외를 던진다.")
        void completePaymentWithInvalidStatus() {
            // arrange
            Payment payment = Payment.create(new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, VALID_PAYMENT_METHOD, VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO));
            payment.markFailed();

            // act && assert
            Assertions.assertThrows(IllegalStateException.class, () -> payment.markCompleted("transactionKey"));
        }
    }

    @Nested
    @DisplayName("Payment를 실패로 마크할 때,")
    class FailPayment {
        @Test
        @DisplayName("결제 상태가 실패 가능한 상태가 아니면, IllegalStateException 예외를 던진다.")
        void failPaymentWithInvalidStatus() {
            // arrange
            Payment payment = Payment.create(new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, VALID_PAYMENT_METHOD, VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO));
            payment.markFailed();

            // act && assert
            Assertions.assertThrows(IllegalStateException.class, payment::markFailed);
        }
    }

    @Nested
    @DisplayName("Payment를 요청 상태로 마크할 때,")
    class RequestPayment {
        @Test
        @DisplayName("트랜잭션 키가 null이면, IllegalArgumentException 예외를 던진다.")
        void requestPaymentWithNullTransactionKey() {
            // arrange
            Payment payment = Payment.create(new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, VALID_PAYMENT_METHOD, VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO));

            // act && assert
            Assertions.assertThrows(IllegalArgumentException.class, () -> payment.markRequested(null));
        }

        @Test
        @DisplayName("결제 상태가 요청 가능한 상태가 아니면, IllegalStateException 예외를 던진다.")
        void requestPaymentWithInvalidStatus() {
            // arrange
            Payment payment = Payment.create(new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, VALID_PAYMENT_METHOD, VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO));
            payment.markCompleted("transactionKey");

            // act && assert
            Assertions.assertThrows(IllegalStateException.class, () -> payment.markRequested("transactionKey"));
        }
    }

    @Nested
    @DisplayName("TransactionKey 없이 Payment를 요청 상태로 마크할 때,")
    class RequestPaymentWithoutTransactionKey {
        @Test
        @DisplayName("결제 상태가 요청 가능한 상태가 아니면, IllegalStateException 예외를 던진다.")
        void requestPaymentWithInvalidStatus() {
            // arrange
            Payment payment = Payment.create(new PaymentCommand.Create(VALID_USER_ID, VALID_ORDER_ID, VALID_PAYMENT_METHOD, VALID_AMOUNT, VALID_CARD_TYPE, VALID_CARD_NO));
            payment.markCompleted("transactionKey");

            // act && assert
            Assertions.assertThrows(IllegalStateException.class, payment::markRequested);
        }
    }
}