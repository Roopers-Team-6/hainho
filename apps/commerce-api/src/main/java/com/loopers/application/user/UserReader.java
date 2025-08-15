package com.loopers.application.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.vo.LoginId;

import java.util.Optional;

public interface UserReader {
    boolean exists(LoginId loginId);

    Optional<User> find(long userId);
}