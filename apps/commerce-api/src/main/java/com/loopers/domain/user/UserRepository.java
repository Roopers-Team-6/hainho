package com.loopers.domain.user;

import com.loopers.domain.user.vo.LoginId;

import java.util.Optional;

public interface UserRepository {
    boolean existsByLoginId(LoginId loginId);

    User save(User user);

    Optional<User> find(long id);
}