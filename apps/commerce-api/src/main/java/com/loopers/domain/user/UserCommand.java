package com.loopers.domain.user;

public class UserCommand {
    public record UserRegisterCommand(
            String userId,
            String gender,
            String email,
            String birthDate
    ) {
    }
}
