package com.loopers.domain.payment;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventPublisher paymentEventPublisher;

    @Nested
    @DisplayName("createPayment 메서드는")
    class CreatePaymentTests {

        @Test
        @DisplayName("유효한 PaymentCommand를 받으면 CardPaymentCreated 이벤트를 발행하고, PaymentInfo를 반환한다.")
        void createPaymentWithValidCommand() {
            // Arrange
            PaymentCommand.Create command = new PaymentCommand.Create(1L, "card", 1000L, "VISA", "1234-5678-9012-3456");
            Payment payment = Payment.create(command);
            PaymentInfo.Get expectedInfo = PaymentInfo.Get.from(payment);
            CardPaymentCreated expectedEvent = CardPaymentCreated.from(payment, command.cardType(), command.cardNumber());

            when(paymentRepository.save(argThat(
                    p -> p.getOrderId().equals(command.orderId()) &&
                            p.getPaymentMethod() == PaymentMethod.CARD &&
                            p.getAmount().equals(PaymentAmount.of(command.amount())) &&
                            p.getStatus() == PaymentStatus.PENDING
            ))).thenReturn(payment);

            doNothing().when(paymentEventPublisher).publish(expectedEvent);

            // Act
            PaymentInfo.Get actualInfo = paymentService.createPayment(command);

            // Assert
            assertThat(actualInfo).isEqualTo(expectedInfo);
            verify(paymentEventPublisher, times(1)).publish(expectedEvent);
        }
    }

    @Nested
    @DisplayName("markResult 메서드는")
    class MarkResultTests {

        @Test
        @DisplayName("결제 상태가 SUCCESS 라면, payment.markRequested를 호출한다.")
        void markResultWithValidCommand() {
            // Arrange
            PaymentCommand.MarkResult command = new PaymentCommand.MarkResult(1L, "transactionKey", "SUCCESS");
            Payment payment = mock(Payment.class);
            when(payment.getStatus()).thenReturn(PaymentStatus.PENDING);
            when(payment.getAmount()).thenReturn(PaymentAmount.of(1000L));
            when(paymentRepository.findByIdWithLock(1L)).thenReturn(Optional.of(payment));

            // Act
            paymentService.markResult(command);

            // Assert
            verify(payment).markRequested(command.transactionKey());
        }

        @Test
        @DisplayName("결제 상태가 FAILED 라면, payment.markFailed를 호출한다.")
        void markResultWithFailedStatus() {
            // Arrange
            PaymentCommand.MarkResult command = new PaymentCommand.MarkResult(1L, "transactionKey", "FAILED");
            Payment payment = mock(Payment.class);
            when(payment.getStatus()).thenReturn(PaymentStatus.PENDING);
            when(payment.getAmount()).thenReturn(PaymentAmount.of(1000L));

            when(paymentRepository.findByIdWithLock(1L)).thenReturn(Optional.of(payment));

            PaymentFailed event = PaymentFailed.of(payment.getOrderId(), payment.getId());
            doNothing().when(paymentEventPublisher).publish(event);

            // Act
            paymentService.markResult(command);

            // Assert
            verify(payment).markFailed();
            verify(paymentEventPublisher, times(1)).publish(any(PaymentFailed.class));
        }

        @Test
        @DisplayName("결제 상태가 READ_TIMEOUT 라면, payment.markRequested를 호출한다.")
        void markResultWithReadTimeoutStatus() {
            // Arrange
            PaymentCommand.MarkResult command = new PaymentCommand.MarkResult(1L, "transactionKey", "READ_TIMEOUT");
            Payment payment = mock(Payment.class);
            when(payment.getStatus()).thenReturn(PaymentStatus.PENDING);
            when(payment.getAmount()).thenReturn(PaymentAmount.of(1000L));

            when(paymentRepository.findByIdWithLock(1L)).thenReturn(Optional.of(payment));

            // Act
            paymentService.markResult(command);

            // Assert
            verify(payment).markRequested();
        }

        @Test
        @DisplayName("유효하지 않은 결제 상태가 주어지면 IllegalArgumentException을 던진다.")
        void markResultWithInvalidStatus() {
            // Arrange
            PaymentCommand.MarkResult command = new PaymentCommand.MarkResult(1L, "transactionKey", "INVALID_STATUS");
            Payment payment = mock(Payment.class);

            when(paymentRepository.findByIdWithLock(1L)).thenReturn(Optional.of(payment));

            // Act && Assert
            assertThrows(IllegalArgumentException.class, () -> paymentService.markResult(command));
        }

        @Test
        @DisplayName("결제 정보를 찾을 수 없으면 EntityNotFoundException을 던진다.")
        void markResultWithNonExistentPayment() {
            // Arrange
            PaymentCommand.MarkResult command = new PaymentCommand.MarkResult(1L, "transactionKey", "SUCCESS");

            when(paymentRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(EntityNotFoundException.class, () -> paymentService.markResult(command));
        }
    }

    @Nested
    @DisplayName("markFinalResult 메서드는")
    class MarkFinalResultTests {

        @Test
        @DisplayName("결제 상태가 SUCCESS 라면, payment.markCompleted를 호출한다.")
        void markFinalResultWithSuccessStatus() {
            // Arrange
            String transactionKey = "transactionKey";
            PaymentCommand.MarkFinalResult command = new PaymentCommand.MarkFinalResult(transactionKey, "SUCCESS");
            Payment payment = mock(Payment.class);
            when(payment.getStatus()).thenReturn(PaymentStatus.REQUESTED);
            when(payment.getAmount()).thenReturn(PaymentAmount.of(1000L));
            when(paymentRepository.findByTransactionKeyWithLock(transactionKey)).thenReturn(Optional.of(payment));

            // Act
            paymentService.markFinalResult(command);

            // Assert
            verify(payment).markCompleted(transactionKey);
        }

        @Test
        @DisplayName("결제 상태가 FAILED 라면, payment.markFailed를 호출한다.")
        void markFinalResultWithFailStatus() {
            // Arrange
            String transactionKey = "transactionKey";
            PaymentCommand.MarkFinalResult command = new PaymentCommand.MarkFinalResult(transactionKey, "FAILED");
            Payment payment = mock(Payment.class);
            when(payment.getStatus()).thenReturn(PaymentStatus.REQUESTED);
            when(payment.getAmount()).thenReturn(PaymentAmount.of(1000L));
            when(paymentRepository.findByTransactionKeyWithLock(transactionKey)).thenReturn(Optional.of(payment));

            // Act
            paymentService.markFinalResult(command);

            // Assert
            verify(payment).markFailed();
        }

        @Test
        @DisplayName("유효하지 않은 결제 상태가 주어지면 IllegalArgumentException을 던진다.")
        void markFinalResultWithInvalidStatus() {
            // Arrange
            PaymentCommand.MarkFinalResult command = new PaymentCommand.MarkFinalResult("transactionKey", "invalid_status");
            Payment payment = mock(Payment.class);
            when(paymentRepository.findByTransactionKeyWithLock("transactionKey")).thenReturn(Optional.of(payment));

            // Act && Assert
            assertThrows(IllegalArgumentException.class, () -> paymentService.markFinalResult(command));
        }

        @Test
        @DisplayName("결제 정보를 찾을 수 없으면 EntityNotFoundException을 던진다.")
        void markFinalResultWithNonExistentPayment() {
            // Arrange
            PaymentCommand.MarkFinalResult command = new PaymentCommand.MarkFinalResult("transactionKey", "success");

            when(paymentRepository.findByTransactionKeyWithLock("transactionKey")).thenReturn(Optional.empty());

            // Act && Assert
            assertThrows(EntityNotFoundException.class, () -> paymentService.markFinalResult(command));
        }
    }

    @Nested
    @DisplayName("verifyPaymentResult 메서드는")
    class VerifyPaymentResultTests {
        private final String orderId = "000001";
        private final String transactionKey = "transactionKey";
        private final String cardType = "cardType";
        private final String cardNo = "cardNo";
        private final Long amount = 1000L;
        private final String reason = "reason";
        private final String status = "SUCCESS";

        @Test
        @DisplayName("결제 결과가 일치하면 아무 예외도 발생하지 않는다.")
        void verifyPaymentResultWithMatchingStatus() {
            // Arrange
            PaymentQuery.Card.Payment command = new PaymentQuery.Card.Payment(transactionKey);
            PaymentInfo.Card.Get paymentInfo = new PaymentInfo.Card.Get(transactionKey, orderId, cardType, cardNo,
                    amount, status, reason);

            when(paymentGateway.findCardPaymentResult(command)).thenReturn(paymentInfo);

            // Act && Assert
            paymentService.verifyPaymentResult(orderId, transactionKey, status);
        }

        @Test
        @DisplayName("결제 결과의 상태가 일치하지 않으면 IllegalStateException을 던진다.")
        void verifyPaymentResultWithMismatchedStatus() {
            // Arrange
            PaymentQuery.Card.Payment command = new PaymentQuery.Card.Payment(transactionKey);
            PaymentInfo.Card.Get paymentInfo = new PaymentInfo.Card.Get(transactionKey, orderId, cardType, cardNo,
                    amount, "FAILED", reason);

            when(paymentGateway.findCardPaymentResult(command)).thenReturn(paymentInfo);

            // Act && Assert
            assertThrows(IllegalStateException.class, () -> paymentService.verifyPaymentResult(orderId, transactionKey, status));
        }

        @Test
        @DisplayName("결제 주문 ID가 일치하지 않으면 IllegalStateException을 던진다.")
        void verifyPaymentResultWithMismatchedOrderId() {
            // Arrange
            PaymentQuery.Card.Payment command = new PaymentQuery.Card.Payment(transactionKey);
            PaymentInfo.Card.Get paymentInfo = new PaymentInfo.Card.Get(transactionKey, "000002", cardType, cardNo,
                    amount, status, reason);

            when(paymentGateway.findCardPaymentResult(command)).thenReturn(paymentInfo);

            // Act && Assert
            assertThrows(IllegalStateException.class, () -> paymentService.verifyPaymentResult(orderId, transactionKey, status));
        }
    }

    @Nested
    @DisplayName("verifyOrderPaymentResult 메서드는")
    class VerifyOrderPaymentResultTests {
        private final Long orderId = 1L;
        private final String stringOrderId = "000001";
        private final String transactionKey = "transactionKey";
        private final String cardType = "cardType";
        private final String cardNo = "cardNo";
        private final Long amount = 1000L;
        private final String reason = "reason";
        private final String status = "SUCCESS";
        private final String failedStatus = "FAILED";

        @Test
        @DisplayName("결제 성공한 PaymentResult가 없으면, FAILED 상태의 PaymentInfo.Card.Result 를 반환한다.")
        void verifyOrderPaymentResultWithNoSuccessfulPayment() {
            // Arrange

            PaymentQuery.Card.Order command = new PaymentQuery.Card.Order(orderId);
            PaymentInfo.Card.Get paymentInfo = new PaymentInfo.Card.Get(transactionKey, stringOrderId, cardType, cardNo,
                    amount, failedStatus, reason);
            PaymentInfo.Card.Order paymentOrder = new PaymentInfo.Card.Order(List.of(paymentInfo));

            when(paymentGateway.findCardOrderResult(command)).thenReturn(paymentOrder);

            // Act
            PaymentInfo.Card.Result result = paymentService.verifyOrderPaymentResult(orderId);

            // Assert
            assertThat(result.status()).isEqualTo("FAILED");
        }

        @Test
        @DisplayName("결제 성공한 PaymentResult가 있으면, 해당 PaymentInfo를 반환한다.")
        void verifyOrderPaymentResultWithSuccessfulPayment() {
            // Arrange
            PaymentQuery.Card.Order command = new PaymentQuery.Card.Order(orderId);
            PaymentInfo.Card.Get paymentInfo = new PaymentInfo.Card.Get(transactionKey, stringOrderId, cardType, cardNo,
                    amount, status, reason);
            PaymentInfo.Card.Order paymentOrder = new PaymentInfo.Card.Order(List.of(paymentInfo));

            when(paymentGateway.findCardOrderResult(command)).thenReturn(paymentOrder);

            PaymentInfo.Card.Result expectedPaymentInfo = paymentInfo.toResult();

            // Act
            PaymentInfo.Card.Result actualPaymentInfo = paymentService.verifyOrderPaymentResult(orderId);

            // Assert
            assertThat(actualPaymentInfo).isEqualTo(expectedPaymentInfo);
        }
    }
}