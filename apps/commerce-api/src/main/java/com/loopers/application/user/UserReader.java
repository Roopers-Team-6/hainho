package com.loopers.application.user;

import com.loopers.domain.user.Point;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserId;

import java.util.Optional;

public interface UserReader {
    boolean exists(UserId userId);

    Optional<User> find(UserId userId);

    Optional<Point> findPoint(UserId userId);
}