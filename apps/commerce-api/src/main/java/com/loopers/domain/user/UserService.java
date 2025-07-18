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
    public boolean exists(LoginId loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> find(long id) {
        return userRepository.find(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Point> findPoint(long id) {
        Optional<User> optionalUser = userRepository.find(id);
        return optionalUser.map(User::getPoint);
    }
}
