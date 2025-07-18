package com.loopers.application.user;

import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.Point;
import com.loopers.domain.user.User;

import java.util.Optional;

public interface UserReader {
    boolean exists(LoginId loginId);

    Optional<User> find(long userId);

    Optional<Point> findPoint(long userId);
}