package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserV1Dto {
    public enum UserV1DtoGender {
        M, F
    }

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
            UserV1DtoGender gender,
            String email,
            String birthDate
    ) {
        public static UserRegisterResponse from(UserInfo userInfo) {
            return new UserRegisterResponse(
                    userInfo.id(),
                    userInfo.userId(),
                    UserV1DtoGender.valueOf(userInfo.gender().toString()),
                    userInfo.email(),
                    userInfo.birthDate()
            );
        }
    }
}