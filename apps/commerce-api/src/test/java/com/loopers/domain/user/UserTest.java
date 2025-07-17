package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static com.loopers.domain.user.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
    @Nested
    @DisplayName("User를 생성할 때,")
    class CreateUser {

        @Nested
        @DisplayName("올바른 값으로 생성하면,")
        class ValidUserCreation {

            // arrange
            private final String validUserId = VALID_USER_ID;
            private final String validGender = VALID_GENDER;
            private final String validEmail = VALID_EMAIL;
            private final String validBirthDate = VALID_BIRTH_DATE;

            @Test
            @DisplayName("정상적으로 User 객체가 생성된다.")
            void createsUserSuccessfully_withValidValues() {
                // act
                User user = createUser(validUserId, validGender, validEmail, validBirthDate);

                // assert
                assertAll(
                        () -> assertThat(user.getUserId().userId()).isEqualTo(validUserId),
                        () -> assertThat(user.getGender()).hasToString(validGender),
                        () -> assertThat(user.getEmail().address()).isEqualTo(validEmail),
                        () -> assertThat(user.getBirthDate().birthDate()).isEqualTo(LocalDate.parse(validBirthDate))
                );
            }

            @Test
            @DisplayName("Point가 0으로 초기화된다.")
            void pointIsInitializedToZero() {
                // act
                User user = createUser(validUserId, validGender, validEmail, validBirthDate);

                // assert
                assertThat(user.getPoint().balance()).isZero();
            }
        }

        @Nested
        @DisplayName("UserId가")
        class UserId {

            @ParameterizedTest
            @DisplayName("영문 및 숫자 10자 이내 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(strings = {
                    "qwer1234567", // 11자
                    "qwer!@#$", // 특수문자 포함
                    " ", // 빈 문자열
            })
            void throwsBadRequestException_whenUserIdExceedsMaxLength(String invalidUserId) {
                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithUserId(invalidUserId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void throwsBadRequestException_whenUserIdIsNull() {
                // arrange
                String nullUserId = null;

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithUserId(nullUserId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("gender가")
        class Gender {

            @ParameterizedTest
            @DisplayName("올바른 값이 아니면, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(strings = {
                    "INVALID", // 잘못된 성별 값
                    " ", // 빈 문자열
            })
            void throwsBadRequestException_whenGenderIsInvalid(String invalidGender) {
                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithGender(invalidGender);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void throwsBadRequestException_whenGenderIsNull() {
                // arrange
                String nullGender = null;

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithGender(nullGender);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("email이")
        class Email {

            @ParameterizedTest
            @DisplayName("xx@yy.zz 형식이 아니면, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(strings = {
                    "invalidEmailFormat", // 잘못된 이메일 형식
                    "user@domain", // 도메인 부분이 없음
                    "user@.com", // 도메인 부분이 잘못됨
                    "user@domain.", // 도메인 끝에 '.'이 있음
                    "userdomain.com", // '@'가 없음
                    " ", // 빈 문자열
            })
            void throwsBadRequestException_whenEmailFormatIsInvalid(String invalidEmail) {
                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithEmail(invalidEmail);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void throwsBadRequestException_whenEmailIsNull() {
                // arrange
                String nullEmail = null;

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithEmail(nullEmail);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("birthDate가")
        class BirthDate {

            @ParameterizedTest
            @DisplayName("yyyy-MM-dd 형식이 아니면, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(strings = {
                    "1990/01/01", // 잘못된 형식 (슬래시 사용)
                    "1990.01.01", // 잘못된 형식 (점 사용)
                    "1990-1-1", // 월과 일이 한 자리 숫자
                    "90-01-01", // 연도가 두 자리 숫자
                    " ", // 빈 문자열
            })
            void throwsBadRequestException_whenBirthDateFormatIsInvalid(String invalidBirthDate) {
                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithBirthDate(invalidBirthDate);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }

            @Test
            @DisplayName("날짜가 가능한 날짜를 초과한 경우, BAD_REQUEST 예외가 발생한다.")
            void throwsBadRequestException_whenBirthDateIsInvalid() {
                // arrange
                String invalidBirthDate = "2025-02-29"; // 2025년은 윤년이 아니므로 2월 29일은 유효하지 않음

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithBirthDate(invalidBirthDate);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void throwsBadRequestException_whenBirthDateIsNull() {
                // arrange
                String nullBirthDate = null;

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithBirthDate(nullBirthDate);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }

            @Test
            @DisplayName("미래 날짜이면, BAD_REQUEST 예외가 발생한다.")
            void throwsBadRequestException_whenBirthDateIsInTheFuture() {
                // arrange
                String futureBirthDate = LocalDate.now().plusDays(2).toString();

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    createUserWithBirthDate(futureBirthDate);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }
    }
}