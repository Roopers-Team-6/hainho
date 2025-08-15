package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point V1 API", description = "유저의 포인트 관련 API입니다.")
public interface PointV1ApiSpec {
    @Operation(
            summary = "포인트 조회",
            description = "유저의 포인트 정보를 조회합니다."
    )
    ApiResponse<PointV1Dto.GetPoint.Response> getPoint(
            @Schema(
                    name = "X-USER-ID",
                    description = "조회할 사용자의 ID"
            )
            Long userId
    );

    @Operation(
            summary = "포인트 충전",
            description = "유저의 포인트를 충전합니다."
    )
    ApiResponse<PointV1Dto.ChargePoint.Response> chargePoint(
            @Schema(
                    name = "X-USER-ID",
                    description = "조회할 사용자의 ID"
            )
            Long userId,
            @Schema(
                    name = "포인트 충전 요청",
                    description = "포인트 충전에 필요한 정보를 담고 있는 요청 객체"
            )
            PointV1Dto.ChargePoint.Request request
    );
}