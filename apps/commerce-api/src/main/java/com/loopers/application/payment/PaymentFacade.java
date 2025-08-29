package com.loopers.application.payment;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.PointService;
import com.loopers.interfaces.api.payment.PaymentV1Dto;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentFacade {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PointService pointService;

    public PaymentV1Dto.CreateWithCard.Response requestCardPayment(PaymentCriteria.Create.Card criteria) {
        OrderInfo.Detail orderInfo = orderService.verifyPayableAndMarkProcessing(criteria.orderId(), criteria.userId());

        PaymentCommand.Create createCommand = new PaymentCommand.Create(criteria.orderId(), "card", orderInfo.discountedPrice());
        PaymentInfo.Get paymentInfo = paymentService.createPayment(createCommand);

        PaymentCommand.Card.Payment command = new PaymentCommand.Card.Payment(criteria.orderId(), criteria.cardType(), criteria.cardNo(), orderInfo.discountedPrice());
        PaymentInfo.Card.Result requestResult = paymentService.requestCardPayment(command);

        PaymentCommand.MarkResult markCommand = new PaymentCommand.MarkResult(paymentInfo.id(), requestResult.transactionKey(), requestResult.status());
        paymentService.markResult(markCommand);

        if (requestResult.status().equals("FAILED")) {
            orderService.markPending(criteria.orderId());
            throw new CoreException(ErrorType.INTERNAL_ERROR, "결제 요청에 실패했습니다. 결제 상태: " + requestResult.status());
        }

        return new PaymentV1Dto.CreateWithCard.Response(orderInfo.discountedPrice());
    }

    public PaymentV1Dto.PaymentCallback.Response verifyCallbackAndCompleteOrder(PaymentCriteria.Callback.Request criteria) {
        // 검증
        paymentService.verifyPaymentResult(criteria.orderId(), criteria.transactionKey(), criteria.status());

        // 검증 결과 반영
        PaymentCommand.MarkFinalResult markCommand = new PaymentCommand.MarkFinalResult(criteria.transactionKey(), criteria.status());
        PaymentInfo.Get markResult = paymentService.markFinalResult(markCommand);

        // 주문 완료 처리
        OrderInfo.Detail orderInfo = orderService.markCompleted(Long.valueOf(criteria.orderId()));
        return new PaymentV1Dto.PaymentCallback.Response(orderInfo.id());
    }

    @Transactional
    public PaymentV1Dto.CreateWithPoint.Response requestPointPayment(PaymentCriteria.Create.Point criteria) {
        OrderInfo.Detail orderInfo = orderService.verifyPayableAndMarkProcessing(criteria.orderId(), criteria.userId());
        pointService.usePoint(criteria.userId(), orderInfo.discountedPrice());
        orderService.markCompleted(criteria.orderId());
        return new PaymentV1Dto.CreateWithPoint.Response(orderInfo.discountedPrice());
    }
}
