package com.loopers.application.user;

import com.loopers.domain.user.User;

public record UserInfo(
        Long id,
        String userId,
        String gender,
        String email,
        String birthDate
) {
    public static UserInfo of(User user) {
        return new UserInfo(
                user.getId(),
                user.getUserId().userId(),
                user.getGender().toString(),
                user.getEmail().address(),
                user.getBirthDate().birthDate().toString()
        );
    }
}
