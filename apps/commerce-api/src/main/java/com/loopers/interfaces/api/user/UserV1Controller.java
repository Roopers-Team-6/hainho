package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserV1Controller implements UserV1ApiSpec {
    private final UserFacade userFacade;

    @Override
    @PostMapping("/users")
    public ApiResponse<UserV1Dto.UserRegisterResponse> registerUser(
            @RequestBody @Valid UserV1Dto.UserRegisterRequest request
    ) {
        UserInfo userInfo = userFacade.registerUser(
                request.userId(), request.gender(), request.email(), request.birthDate());
        UserV1Dto.UserRegisterResponse response = UserV1Dto.UserRegisterResponse.from(userInfo);
        return ApiResponse.success(response);
    }

    @Override
    @GetMapping("/users/me")
    public ApiResponse<UserV1Dto.UserResponse> getUser(
            @RequestHeader(value = "X-USER-ID") String userId
    ) {
        UserInfo userInfo = userFacade.getUser(userId);
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userInfo);
        return ApiResponse.success(response);
    }

    @Override
    @GetMapping("/points")
    public ApiResponse<UserV1Dto.PointResponse> getPoint(
            @RequestHeader(value = "X-USER-ID") String userId
    ) {
        Long point = userFacade.getPoint(userId);
        UserV1Dto.PointResponse response = new UserV1Dto.PointResponse(point);
        return ApiResponse.success(response);
    }

    @Override
    @PostMapping("/points/charge")
    public ApiResponse<UserV1Dto.PointChargeResponse> chargePoint(
            @RequestHeader(value = "X-USER-ID") String userId,
            @RequestBody @Valid UserV1Dto.PointChargeRequest request
    ) {
        Long point = userFacade.chargePoint(userId, request.point());
        UserV1Dto.PointChargeResponse response = new UserV1Dto.PointChargeResponse(point);
        return ApiResponse.success(response);
    }
}