package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCriteria;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentV1Controller implements PaymentV1ApiSpec {
    private final PaymentFacade paymentFacade;

    @Override
    @PostMapping("/payments/card")
    public ApiResponse<PaymentV1Dto.CreateWithCard.Response> requestCardPayment(
            @RequestBody PaymentV1Dto.CreateWithCard.Request request,
            @RequestHeader(name = "X-USER-ID") Long userId
    ) {
        PaymentCriteria.Create.Card criteria = request.toCriteria(userId);
        PaymentV1Dto.CreateWithCard.Response response = paymentFacade.requestCardPayment(criteria);
        return ApiResponse.success(response);
    }

    @Override
    @PostMapping("/payments/point")
    public ApiResponse<PaymentV1Dto.CreateWithPoint.Response> requestPointPayment(
            @RequestBody PaymentV1Dto.CreateWithPoint.Request request,
            @RequestHeader(name = "X-USER-ID") Long userId
    ) {
        PaymentCriteria.Create.Point criteria = request.toCriteria(userId);
        PaymentV1Dto.CreateWithPoint.Response response = paymentFacade.requestPointPayment(criteria);
        return ApiResponse.success(response);
    }

    @Override
    @PostMapping("/payments/callback")
    public ApiResponse<PaymentV1Dto.PaymentCallback.Response> paymentCallback(
            @RequestBody PaymentV1Dto.PaymentCallback.Request request
    ) {
        PaymentCriteria.Callback.Request criteria = request.toCriteria();
        PaymentV1Dto.PaymentCallback.Response response = paymentFacade.verifyCallbackAndCompleteOrder(criteria);
        return ApiResponse.success(response);
    }
}
