package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Nested
    @DisplayName("Product를 생성할 때,")
    class CreateProductTest {

        @Nested
        @DisplayName("price가")
        class PriceTest {

            @ParameterizedTest
            @DisplayName("1 미만의 값이 주어지면, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(longs = {
                    0, -1, -100, Long.MIN_VALUE
            })
            void priceLessThanOneThrowsBadRequest(long price) {
                // arrange

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    ProductFixture.createProductWithPrice(price);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("name이")
        class NameTest {

            @ParameterizedTest
            @DisplayName("2~20자 이내의 영어, 숫자, 한글(공백 포함 가능)이 아닌 경우, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(strings = {
                    "  ", // 길이 2의 blank 문자열
                    "a", // 2자 미만
                    "123456789 123456789 1", // 20자 초과
                    "abcde!@#$%^&*()" // 특수문자 포함
            })
            @NullAndEmptySource
                // null 또는 빈 문자열도 테스트
            void nameInvalidThrowsBadRequest(String name) {
                // arrange

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    ProductFixture.createProductWithName(name);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("description이")
        class DescriptionTest {

            @ParameterizedTest
            @DisplayName("10~50자 이내의 영어, 숫자, 한글(공백 포함 가능)가 아닌 경우, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(strings = {
                    "          ", // 길이 10의 blank 문자열
                    "123456789", // 10자 미만
                    "123456789 123456789 123456789 123456789 123456789 1", // 50자 초과
                    "abcde!@#$%^&*()" // 특수문자 포함
            })
            @NullAndEmptySource
                // null 또는 빈 문자열도 테스트
            void descriptionInvalidThrowsBadRequest(String description) {
                // arrange

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    ProductFixture.createProductWithDescription(description);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("brandId가")
        class BrandIdTest {

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void brandIdNullThrowsBadRequest() {
                // arrange
                Long brandId = null;

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    ProductFixture.createProductWithBrandId(brandId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }
    }
}