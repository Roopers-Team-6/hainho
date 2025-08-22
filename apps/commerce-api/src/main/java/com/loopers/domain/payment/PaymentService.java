package com.loopers.domain.payment;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    @Transactional
    public PaymentInfo.Get createPayment(PaymentCommand.Create command) {
        Payment payment = Payment.create(command);
        return PaymentInfo.Get.from(payment);
    }

    @Transactional
    public PaymentInfo.Get markResult(PaymentCommand.MarkResult command) {
        Payment payment = getPaymentWithLock(command.paymentId());
        markRequestStatus(payment, command);
        return PaymentInfo.Get.from(payment);
    }

    private Payment getPaymentWithLock(Long paymentId) {
        return paymentRepository.findByIdWithLock(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("결제를 찾을 수 없습니다. ID: " + paymentId));
    }

    private void markRequestStatus(Payment payment, PaymentCommand.MarkResult command) {
        if (command.resultStatus().equals("SUCCESS")) {
            payment.markRequested(command.transactionKey());
        } else if (command.resultStatus().equals("READ_TIMEOUT")) {
            payment.markRequested();
        } else if (command.resultStatus().equals("FAILED")) {
            payment.markFailed();
        } else {
            throw new IllegalArgumentException("결제 결과 상태가 유효하지 않습니다: " + command.resultStatus());
        }
    }

    @Transactional
    public PaymentInfo.Get markFinalResult(PaymentCommand.MarkFinalResult command) {
        Payment payment = getPaymentWithLock(command.transactionKey());
        markFinalStatus(payment, command);
        return PaymentInfo.Get.from(payment);
    }

    private Payment getPaymentWithLock(String transactionKey) {
        return paymentRepository.findByTransactionKeyWithLock(transactionKey)
                .orElseThrow(() -> new EntityNotFoundException("결제를 찾을 수 없습니다. TransactionKey: " + transactionKey));
    }

    private void markFinalStatus(Payment payment, PaymentCommand.MarkFinalResult command) {
        if (command.resultStatus().equals("SUCCESS")) {
            payment.markCompleted(command.transactionKey());
        } else if (command.resultStatus().equals("FAILED")) {
            payment.markFailed();
        } else {
            throw new IllegalArgumentException("결제 결과 상태가 유효하지 않습니다: " + command.resultStatus());
        }
    }

    public void verifyPaymentResult(String orderId, String transactionKey, String status) {
        PaymentQuery.Card.Payment command = new PaymentQuery.Card.Payment(transactionKey);
        PaymentInfo.Card.Get paymentInfo = paymentGateway.findCardPaymentResult(command);
        if (!paymentInfo.status().equals(status)) {
            throw new IllegalStateException("결제 상태가 일치하지 않습니다. 요청된 상태: " + status + ", 실제 상태: " + paymentInfo.status());
        }
        if (!paymentInfo.orderId().equals(orderId)) {
            throw new IllegalStateException("결제 주문 ID가 일치하지 않습니다. 요청된 주문 ID: " + orderId + ", 실제 주문 ID: " + paymentInfo.orderId());
        }
    }

    public PaymentInfo.Card.Result requestCardPayment(PaymentCommand.Card.Payment command) {
        return paymentGateway.requestCardPayment(command);
    }

    public PaymentInfo.Card.Get getCardPayment(PaymentQuery.Card.Payment query) {
        return paymentGateway.findCardPaymentResult(query);
    }

    public PaymentInfo.Card.Order getCardOrder(PaymentQuery.Card.Order query) {
        return paymentGateway.findCardOrderResult(query);
    }

    public PaymentInfo.Card.Result verifyOrderPaymentResult(Long orderId) {
        PaymentQuery.Card.Order query = new PaymentQuery.Card.Order(orderId);
        PaymentInfo.Card.Order cardOrder = paymentGateway.findCardOrderResult(query);
        List<PaymentInfo.Card.Get> successPaymentInfo = cardOrder.paymentResults().stream()
                .filter(paymentResult -> paymentResult.status().equals("SUCCESS")).toList();
        if (successPaymentInfo.isEmpty()) {
            return new PaymentInfo.Card.Result(
                    null,
                    "FAILED",
                    "결제 성공 내역이 없습니다. 주문 ID: " + orderId
            );
        }
        if (successPaymentInfo.size() > 1) {
            // 환불 담당자에게 알림 전송
        }
        PaymentInfo.Card.Get paymentInfo = successPaymentInfo.get(0);
        return paymentInfo.toResult();
    }
}
