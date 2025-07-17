package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {
    boolean existsByUserId(UserId userId);

    User save(User user);

    Optional<User> find(long id);
}