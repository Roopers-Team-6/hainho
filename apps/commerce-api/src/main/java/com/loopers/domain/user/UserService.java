package com.loopers.domain.user;

import com.loopers.application.user.UserReader;
import com.loopers.domain.user.vo.LoginId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
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
}
