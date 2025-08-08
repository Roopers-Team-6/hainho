package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserV1Controller implements UserV1ApiSpec {
    private final UserFacade userFacade;

    @Override
    @PostMapping("/users")
    public ApiResponse<UserV1Dto.UserRegisterResponse> registerUser(
            @RequestBody @Valid UserV1Dto.UserRegisterRequest request
    ) {
        UserInfo userInfo = userFacade.registerUser(
                request.loginId(), request.gender(), request.email(), request.birthDate());
        UserV1Dto.UserRegisterResponse response = UserV1Dto.UserRegisterResponse.from(userInfo);
        return ApiResponse.success(response);
    }

    @Override
    @GetMapping("/users/me")
    public ApiResponse<UserV1Dto.UserResponse> getUser(
            @RequestHeader(value = "X-USER-ID") Long userId
    ) {
        UserInfo userInfo = userFacade.getUser(userId);
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userInfo);
        return ApiResponse.success(response);
    }
}