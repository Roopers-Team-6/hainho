package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserV1Dto {
    public record UserRegisterRequest(
            @NotNull
            String userId,
            @NotNull
            String gender,
            @Email
            String email,
            @NotNull
            String birthDate
    ) {
    }

    public record UserRegisterResponse(
            Long id,
            String userId,
            String gender,
            String email,
            String birthDate
    ) {
        public static UserRegisterResponse from(UserInfo userInfo) {
            return new UserRegisterResponse(
                    userInfo.id(),
                    userInfo.userId(),
                    userInfo.gender(),
                    userInfo.email(),
                    userInfo.birthDate()
            );
        }
    }
}