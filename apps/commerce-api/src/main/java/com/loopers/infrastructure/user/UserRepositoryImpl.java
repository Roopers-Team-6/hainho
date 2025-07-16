package com.loopers.infrastructure.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserId;
import com.loopers.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean existsByUserId(UserId userId) {
        return userJpaRepository.existsByUserId(userId);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
