package com.loopers.interfaces.api.payment;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Payment V1 API", description = "결제 관련 API입니다.")
public interface PaymentV1ApiSpec {
    @Operation(
            summary = "카드 결제 요청",
            description = "카드 결제를 요청하는 API입니다. " +
                    "결제 정보를 포함한 요청을 받아 PG사에 결제를 요청합니다."
    )
    ApiResponse<PaymentV1Dto.CreateWithCard.Response> requestCardPayment(
            PaymentV1Dto.CreateWithCard.Request request,
            Long userId
    );

    @Operation(
            summary = "포인트 결제 요청",
            description = "포인트 결제를 요청하는 API입니다."
    )
    ApiResponse<PaymentV1Dto.CreateWithPoint.Response> requestPointPayment(
            PaymentV1Dto.CreateWithPoint.Request request,
            Long userId
    );

    @Operation(
            summary = "결제 완료 콜백",
            description = "결제 완료 후 PG사에서 호출되는 콜백 API입니다. " +
                    "결제 상태를 업데이트하고, 주문 상태를 변경합니다."
    )
    ApiResponse<PaymentV1Dto.PaymentCallback.Response> paymentCallback(
            PaymentV1Dto.PaymentCallback.Request request
    );
}
