package com.loopers.domain.user;

import com.loopers.application.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserService implements UserReader {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UserId userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> find(UserId userId) {
        return userRepository.find(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Point> findPoint(UserId userId) {
        Optional<User> optionalUser = userRepository.find(userId);
        return optionalUser.map(User::getPoint);
    }
}
