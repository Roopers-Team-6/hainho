package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Order V1 API", description = "주문 관련 API입니다.")
public interface OrderV1ApiSpec {
    @Operation(
            summary = "주문 생성",
            description = "상품의 재고를 차감하고, 유저의 포인트를 차감하고, 쿠폰을 사용하여 주문을 생성합니다. "
    )
    ApiResponse<OrderV1Dto.CreateOrder.Response> createOrder(
            @Schema(
                    name = "X-USER-ID",
                    description = "조회할 사용자의 ID"
            )
            Long userId,
            @Schema(
                    name = "주문 생성 요청",
                    description = "주문 생성에 필요한 정보를 담고 있는 요청 객체"
            )
            OrderV1Dto.CreateOrder.Request request
    );
}
