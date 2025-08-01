package com.loopers.domain.like;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LikeProductTest {

    @Nested
    @DisplayName("LikeProduct를 생성할 때,")
    class CreateLikeProductTest {

        @Nested
        @DisplayName("모든 값이 유효하면,")
        class AllValuesTest {
            @Test
            @DisplayName("LikeProduct 객체가 생성된다.")
            void whenUserIdAndProductIdAreValidCreatesLikeProduct() {
                // arrange
                Long userId = 1L;
                Long productId = 1L;

                // act
                LikeProduct likeProduct = LikeProduct.create(userId, productId);

                // assert
                assertThat(likeProduct).isNotNull();
                assertThat(likeProduct.getUserId()).isEqualTo(userId);
                assertThat(likeProduct.getProductId()).isEqualTo(productId);
            }
        }

        @Nested
        @DisplayName("userId가")
        class UserIdTest {

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void whenUserIdIsNullThrowsBadRequest() {
                // arrange
                Long userId = null;
                Long productId = 1L;

                // act & assert
                CoreException result = assertThrows(CoreException.class, () -> {
                    LikeProduct.create(userId, productId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("productId가")
        class ProductIdTest {

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void whenProductIdIsNullThrowsBadRequest() {
                // arrange
                Long userId = 1L;
                Long productId = null;

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    LikeProduct.create(userId, productId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }
    }
}