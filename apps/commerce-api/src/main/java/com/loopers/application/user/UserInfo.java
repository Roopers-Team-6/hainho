package com.loopers.application.user;

import com.loopers.domain.user.User;

public record UserInfo(
        Long id,
        String loginId,
        String gender,
        String email,
        String birthDate
) {
    public static UserInfo of(User user) {
        return new UserInfo(
                user.getId(),
                user.getLoginId().getLoginId(),
                user.getGender().toString(),
                user.getEmail().getAddress(),
                user.getBirthDate().getBirthDate().toString()
        );
    }
}
