package com.loopers.domain.payment;

public interface PaymentGateway {
    PaymentInfo.Card.Result requestCardPayment(PaymentCommand.Card.Payment command);

    PaymentInfo.Card.Get findCardPaymentResult(PaymentQuery.Card.Payment query);

    PaymentInfo.Card.Order findCardOrderResult(PaymentQuery.Card.Order query);
}
