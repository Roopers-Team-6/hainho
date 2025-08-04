package com.loopers.domain.brand;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BrandTest {

    @Nested
    @DisplayName("Brand를 생성할 때,")
    class CreateBrandTest {

        @Nested
        @DisplayName("name이")
        class NameTest {

            @ParameterizedTest
            @DisplayName("1~20자 이내의 영어, 숫자, 한글(공백 포함 가능)이 아닌 경우, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(strings = {
                    " ", // 길이 1의 blank 문자열
                    "123456789 123456789 1", // 20자 초과
                    "abcde!@#$%^&*()" // 특수문자 포함
            })
            @NullAndEmptySource
                // null 또는 빈 문자열도 테스트
            void nameInvalidThrowsBadRequest(String name) {
                // arrange

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    BrandFixture.createBrandWithName(name);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("description이")
        class DescriptionTest {

            @ParameterizedTest
            @DisplayName("10~50자 이내의 영어, 숫자, 한글(공백 포함 가능)이 아닌 경우, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(strings = {
                    "          ", // 길이 10의 blank 문자열
                    "123456789 123456789 123456789 123456789 123456789 1", // 50자 초과
                    "abcde!@#$%^&*()" // 특수문자 포함
            })
            @NullAndEmptySource
                // null 또는 빈 문자열도 테스트
            void descriptionInvalidThrowsBadRequest(String description) {
                // arrange

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    BrandFixture.createBrandWithDescription(description);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }
    }
}