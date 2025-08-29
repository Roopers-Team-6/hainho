package com.loopers.application.payment;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.PointService;
import com.loopers.interfaces.api.payment.PaymentV1Dto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentFacade {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PointService pointService;

    public PaymentV1Dto.CreateWithCard.Response requestCardPayment(PaymentCriteria.Create.Card criteria) {
        OrderInfo.Detail orderInfo = orderService.verifyPayableAndMarkProcessing(criteria.orderId(), criteria.userId());

        PaymentCommand.Create createCommand = new PaymentCommand.Create(criteria.userId(), criteria.orderId(), "card", orderInfo.discountedPrice(), criteria.cardType(), criteria.cardNo());
        paymentService.createCardPayment(createCommand);

        return new PaymentV1Dto.CreateWithCard.Response(orderInfo.discountedPrice());
    }

    public PaymentV1Dto.PaymentCallback.Response verifyCallbackAndCompleteOrder(PaymentCriteria.Callback.Request criteria) {
        paymentService.verifyPaymentResult(criteria.orderId(), criteria.transactionKey(), criteria.status());
        return new PaymentV1Dto.PaymentCallback.Response(criteria.orderId());
    }

    public PaymentV1Dto.CreateWithPoint.Response requestPointPayment(PaymentCriteria.Create.Point criteria) {
        OrderInfo.Detail orderInfo = orderService.verifyPayableAndMarkProcessing(criteria.orderId(), criteria.userId());

        PaymentCommand.Create createCommand = new PaymentCommand.Create(criteria.userId(), criteria.orderId(), "point", orderInfo.discountedPrice(), null, null);
        paymentService.createPointPayment(createCommand);
        return new PaymentV1Dto.CreateWithPoint.Response(orderInfo.discountedPrice());
    }
}
