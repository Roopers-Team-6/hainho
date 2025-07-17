package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "Loopers 사용자 관련 API입니다.")
public interface UserV1ApiSpec {

    @Operation(
            summary = "유저 등록",
            description = "사용자를 등록합니다."
    )
    ApiResponse<UserV1Dto.UserRegisterResponse> registerUser(
            @Schema(
                    name = "사용자 등록 요청",
                    description = "사용자 등록에 필요한 정보를 담고 있는 요청 객체"
            )
            UserV1Dto.UserRegisterRequest userRequest
    );

    @Operation(
            summary = "유저 조회",
            description = "UserID로 유저 정보를 조회합니다."
    )
    ApiResponse<UserV1Dto.UserResponse> getUser(
            @Schema(
                    name = "X-USER-ID",
                    description = "조회할 사용자의 ID"
            )
            String userId
    );

    @Operation(
            summary = "포인트 조회",
            description = "유저의 포인트 정보를 조회합니다."
    )
    ApiResponse<UserV1Dto.PointResponse> getPoint(
            @Schema(
                    name = "X-USER-ID",
                    description = "조회할 사용자의 ID"
            )
            String userId
    );
}