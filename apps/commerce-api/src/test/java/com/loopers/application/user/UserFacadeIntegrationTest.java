package com.loopers.application.user;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserFixture;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserFacadeIntegrationTest {

    @Autowired
    private UserFacade userFacade;

    @MockitoSpyBean
    private UserRepository userRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("유저 회원가입 시,")
    class RegisterUser {
        @Test
        @DisplayName("성공하면, User가 저장된다.")
        void savesUserSuccessfully() {
            // arrange
            String userId = UserFixture.VALID_USER_ID;
            String gender = UserFixture.VALID_GENDER;
            String email = UserFixture.VALID_EMAIL;
            String birthDate = UserFixture.VALID_BIRTH_DATE;

            // act
            UserInfo userInfo = userFacade.registerUser(userId, gender, email, birthDate);

            // assert
            assertAll(
                    () -> assertThat(userInfo).isNotNull(),
                    () -> assertThat(userInfo.id()).isNotNull(),
                    () -> assertThat(userInfo.userId()).isEqualTo(userId),
                    () -> assertThat(userInfo.gender()).hasToString(gender),
                    () -> assertThat(userInfo.email()).isEqualTo(email),
                    () -> assertThat(userInfo.birthDate()).isEqualTo(birthDate)
            );
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("중복된 UserId가 있으면, BAD_REQUEST 예외가 발생한다.")
        void throwsExceptionWhenDuplicateUserId() {
            // arrange
            String duplicateUserId = "qwer1234";
            String gender = UserFixture.VALID_GENDER;
            String email = UserFixture.VALID_EMAIL;
            String email2 = "email2@gmail.com";
            String birthDate = UserFixture.VALID_BIRTH_DATE;

            userFacade.registerUser(duplicateUserId, gender, email, birthDate); // 첫 번째 등록

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                userFacade.registerUser(duplicateUserId, gender, email2, birthDate); // 중복 등록 시도
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            verify(userRepository, times(1)).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("포인트 충전 시,")
    class ChargePoint {
        @Test
        @DisplayName("존재하지 않는 user.id 충전 시, NotFound 예외가 발생한다.")
        void throwsNotFoundExceptionWhenUserNotFound() {
            // arrange
            long nonExistingUserId = 1L;
            long chargeAmount = 1000;

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                userFacade.chargePoint(nonExistingUserId, chargeAmount);
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @Test
        @DisplayName("존재하는 user.id로 포인트를 충전하면, 충전된 포인트를 반환한다.")
        void chargesPointSuccessfully() {
            // arrange
            String userId = UserFixture.VALID_USER_ID;
            String gender = UserFixture.VALID_GENDER;
            String email = UserFixture.VALID_EMAIL;
            String birthDate = UserFixture.VALID_BIRTH_DATE;

            UserInfo userInfo = userFacade.registerUser(userId, gender, email, birthDate);
            long id = userInfo.id();
            long chargeAmount = 1000;

            // act
            Long chargedPoint = userFacade.chargePoint(id, chargeAmount);

            // assert
            assertAll(
                    () -> assertThat(chargedPoint).isNotNull(),
                    () -> assertThat(chargedPoint).isEqualTo(chargeAmount)
            );
        }
    }
}
