package com.loopers.domain.user;

public interface UserRepository {
    boolean existsByUserId(UserId userId);

    User save(User user);
}