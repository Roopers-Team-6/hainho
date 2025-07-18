package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    boolean existsByLoginId(LoginId loginId);

    User save(User user);

    Optional<User> find(long id);
}