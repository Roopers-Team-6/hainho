package com.loopers.domain.product;

import com.loopers.domain.like.LikeProductCount;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class LikeProductCountTest {

    @Nested
    @DisplayName("ProductLikeCount를 생성할 때,")
    class CreateLikeProductCountTest {

        @Nested
        @DisplayName("productId가")
        class ProductIdTest {

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void whenProductIdIsNullThrowsBadRequest() {
                // arrange
                Long productId = null;

                // act & assert
                CoreException result = assertThrows(CoreException.class, () -> {
                    LikeProductCount.create(productId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("valid한 값이 주어지면,")
        class ValidValuesTest {

            @Test
            @DisplayName("likeCount값이 0으로, ProductLikeCount 객체가 생성된다.")
            void whenValidValuesThenCreatesProductLikeCount() {
                // arrange
                Long productId = 1L;

                // act
                LikeProductCount likeProductCount = LikeProductCount.create(productId);

                // assert
                assertThat(likeProductCount).isNotNull();
                assertThat(likeProductCount.getProductId()).isEqualTo(productId);
                assertThat(likeProductCount.getCountValue().getValue()).isZero();
            }
        }
    }

}