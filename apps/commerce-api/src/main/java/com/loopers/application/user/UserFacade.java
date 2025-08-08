package com.loopers.application.user;

import com.loopers.domain.point.PointService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.vo.LoginId;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFacade {
    private final UserReader userReader;
    private final UserRepository userRepository;
    private final PointService pointService;

    @Transactional
    public UserInfo registerUser(String loginId, String gender, String email, String birthDate) {
        checkDuplicateLoginId(loginId);
        User user = User.register(loginId, gender, email, birthDate);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        pointService.createPoint(userId);
        return UserInfo.of(savedUser);
    }

    private void checkDuplicateLoginId(String loginId) {
        if (userReader.exists(LoginId.of(loginId))) {
            throw new CoreException(ErrorType.BAD_REQUEST, "LoginId가 이미 존재합니다.");
        }
    }

    public UserInfo getUser(long userId) {
        User user = userReader.find(userId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "id에 해당하는 User를 찾을 수 없습니다."));
        return UserInfo.of(user);
    }
}
