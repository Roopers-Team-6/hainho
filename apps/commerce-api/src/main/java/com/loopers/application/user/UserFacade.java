package com.loopers.application.user;

import com.loopers.domain.user.Point;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserId;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserReader userReader;
    private final UserRepository userRepository;

    @Transactional
    public UserInfo registerUser(String userId, String gender, String email, String birthDate) {
        checkDuplicateUserId(userId);
        User user = User.register(userId, gender, email, birthDate);
        return UserInfo.of(userRepository.save(user));
    }

    private void checkDuplicateUserId(String userId) {
        if (userReader.exists(new UserId(userId))) {
            throw new CoreException(ErrorType.BAD_REQUEST, "UserId가 이미 존재합니다.");
        }
    }

    public UserInfo getUser(long userId) {
        User user = userReader.find(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "id에 해당하는 User를 찾을 수 없습니다."));
        return UserInfo.of(user);
    }

    public Long getPoint(long userId) {
        Point point = userReader.findPoint(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "id에 해당하는 User를 찾을 수 없습니다."));
        return point.balance();
    }

    @Transactional
    public Long chargePoint(long userId, Long point) {
        User user = userReader.find(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "id에 해당하는 User를 찾을 수 없습니다."));
        user.chargePoint(point);
        return user.getPoint().balance();
    }
}
