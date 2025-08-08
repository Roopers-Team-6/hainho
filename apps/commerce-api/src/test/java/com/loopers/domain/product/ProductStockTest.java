package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductStockTest {
    @Nested
    @DisplayName("ProductStock 생성할 때")
    class Create {

        @Nested
        @DisplayName("productId가")
        class ProductId {

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다")
            void whenProductIdIsNullThrowsBadRequest() {
                // arrange
                Long productId = null;

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    ProductStockFixture.createProductStockWithProductId(productId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("stock이")
        class Stock {

            @ParameterizedTest
            @DisplayName("없거나, 0보다 작으면, IllegalArgumentException 예외가 발생한다")
            @ValueSource(longs = {-1L, -100L})
            @NullSource
                // null도 테스트
            void whenStockIsNullOrLessThanZeroThrowsIllegalArgumentException(Long stock) {
                // act && assert
                assertThrows(IllegalArgumentException.class, () -> {
                    ProductStockFixture.createProductStockWithQuantity(stock);
                });
            }
        }
    }

    @Nested
    @DisplayName("재고를 차감할 때")
    class DeductStock {

        @ParameterizedTest
        @DisplayName("차감하려는 값이 없거나 1 미만인 경우, IllegalArgumentException 예외가 발생한다")
        @ValueSource(longs = {0L, -1L, -100L})
        @NullSource
            // null도 테스트
        void whenDeductingInvalidStockThrowsIllegalArgumentException(Long quantityToDeduct) {
            // arrange
            ProductStock productStock = ProductStockFixture.createProductStockWithQuantity(10L);

            // act && assert
            assertThrows(IllegalArgumentException.class, () -> {
                productStock.deduct(quantityToDeduct);
            });
        }

        @Test
        @DisplayName("남은 재고가 차감하려는 수량보다 적은 경우, IllegalStateException 예외가 발생한다")
        void whenDeductingMoreThanAvailableStockThrowsIllegalStateException() {
            // arrange
            Long quantity = 10L;
            ProductStock productStock = ProductStockFixture.createProductStockWithQuantity(quantity);
            Long quantityToDeduct = quantity + 1; // 남은 재고보다 1개 더 차감하려고 함

            // act && assert
            assertThrows(IllegalStateException.class, () -> {
                productStock.deduct(quantityToDeduct);
            });
        }

        @Test
        @DisplayName("남은 재고가 차감하려는 수량 이상인 경우, 재고가 정상적으로 차감된다")
        void whenDeductingValidStockThenStockIsReduced() {
            // arrange
            Long initialQuantity = 10L;
            ProductStock productStock = ProductStockFixture.createProductStockWithQuantity(initialQuantity);
            Long quantityToDeduct = 1L; // 차감하려는 수량
            Long expectedQuantityAfterDeduct = initialQuantity - quantityToDeduct;

            // act
            productStock.deduct(quantityToDeduct);

            // assert
            assertThat(productStock.getQuantity().getValue()).isEqualTo(expectedQuantityAfterDeduct);
        }
    }
}