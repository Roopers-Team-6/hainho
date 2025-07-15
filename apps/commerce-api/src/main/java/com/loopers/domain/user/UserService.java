package com.loopers.domain.user;

import com.loopers.application.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserService implements UserReader {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UserId userId) {
        return userRepository.existsByUserId(userId);
    }
}
