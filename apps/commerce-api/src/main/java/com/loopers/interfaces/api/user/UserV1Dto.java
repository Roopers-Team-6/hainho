package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UserV1Dto {
    public record UserRegisterRequest(
            @NotNull
            String loginId,
            @NotNull
            String gender,
            @Email
            String email,
            @NotNull
            String birthDate
    ) {
    }

    public record PointChargeRequest(
            @Min(value = 1, message = "충전할 포인트는 1 이상이어야 합니다.")
            Long point
    ) {
    }

    public record UserRegisterResponse(
            Long id,
            String loginId,
            String gender,
            String email,
            String birthDate
    ) {
        public static UserRegisterResponse from(UserInfo userInfo) {
            return new UserRegisterResponse(
                    userInfo.id(),
                    userInfo.loginId(),
                    userInfo.gender(),
                    userInfo.email(),
                    userInfo.birthDate()
            );
        }
    }

    public record UserResponse(
            Long id,
            String loginId,
            String gender,
            String email,
            String birthDate
    ) {
        public static UserResponse from(UserInfo userInfo) {
            return new UserResponse(
                    userInfo.id(),
                    userInfo.loginId(),
                    userInfo.gender(),
                    userInfo.email(),
                    userInfo.birthDate()
            );
        }
    }

    public record PointResponse(
            Long point
    ) {
    }

    public record PointChargeResponse(
            Long point
    ) {
    }
}