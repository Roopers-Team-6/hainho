package com.loopers.application.user;

import com.loopers.domain.user.UserId;

public interface UserReader {
    boolean exists(UserId userId);
}