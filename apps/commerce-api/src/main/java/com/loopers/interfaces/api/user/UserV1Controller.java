package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.UserCommand;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {
    private final UserFacade userFacade;

    @Override
    @PostMapping
    public ApiResponse<UserV1Dto.UserRegisterResponse> registerUser(
            @RequestBody @Valid UserV1Dto.UserRegisterRequest request
    ) {
        UserInfo userInfo = userFacade.registerUser(
                new UserCommand.UserRegisterCommand(
                        request.userId(), request.gender(), request.email(), request.birthDate()));
        UserV1Dto.UserRegisterResponse response = UserV1Dto.UserRegisterResponse.from(userInfo);
        return ApiResponse.success(response);
    }
}