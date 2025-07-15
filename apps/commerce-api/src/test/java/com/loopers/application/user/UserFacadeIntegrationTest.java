package com.loopers.application.user;

import com.loopers.domain.user.*;
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
            UserCommand.UserRegisterCommand command = UserFixture.createUserRegisterCommand();

            // act
            UserInfo userInfo = userFacade.registerUser(command);

            // assert
            assertAll(
                    () -> assertThat(userInfo).isNotNull(),
                    () -> assertThat(userInfo.id()).isNotNull(),
                    () -> assertThat(userInfo.userId()).isEqualTo(command.userId()),
                    () -> assertThat(userInfo.gender()).hasToString(command.gender()),
                    () -> assertThat(userInfo.email()).isEqualTo(command.email()),
                    () -> assertThat(userInfo.birthDate()).isEqualTo(command.birthDate())
            );
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("중복된 UserId가 있으면, BAD_REQUEST 예외가 발생한다.")
        void throwsExceptionWhenDuplicateUserId() {
            // arrange
            String duplicateUserId = "qwer1234";
            UserCommand.UserRegisterCommand command = new UserCommand.UserRegisterCommand(
                    duplicateUserId, Gender.M.toString(), "email1@gmail.com", "1990-01-01"
            );
            UserCommand.UserRegisterCommand duplicatedCommand = new UserCommand.UserRegisterCommand(
                    duplicateUserId, Gender.M.toString(), "email2@gmail.com", "1990-01-01"
            );
            userFacade.registerUser(command); // 첫 번째 등록

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                userFacade.registerUser(duplicatedCommand); // 중복 등록 시도
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            verify(userRepository, times(1)).save(any(User.class));
        }
    }
}
