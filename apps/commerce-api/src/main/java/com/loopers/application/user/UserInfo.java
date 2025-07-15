package com.loopers.application.user;

import com.loopers.domain.user.User;

public record UserInfo(
        Long id,
        String userId,
        UserInfoGender gender,
        String email,
        String birthDate
) {
    public static UserInfo of(User user) {
        return new UserInfo(
                user.getId(),
                user.getUserId().userId(),
                UserInfoGender.valueOf(user.getGender().toString()),
                user.getEmail().address(),
                user.getBirthDate().birthDate().toString()
        );
    }

    public enum UserInfoGender {
        M, F
    }
}
