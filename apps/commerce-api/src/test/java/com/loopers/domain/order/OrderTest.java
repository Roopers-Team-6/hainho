package com.loopers.domain.order;


import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class OrderTest {

    @Nested
    @DisplayName("Order를 생성할 때,")
    class CreateOrder {

        @Test
        @DisplayName("유효한 값이 주어지면, Order 객체가 생성된다.")
        void whenValidValuesThenCreatesOrder() {
            // arrange
            Long userId = 1L;
            Long totalPrice = 10000L;

            // act
            Order order = Order.create(userId, totalPrice);

            // assert
            assertThat(order).isNotNull();
            assertThat(order.getUserId()).isEqualTo(userId);
            assertThat(order.getTotalPrice().getValue()).isEqualTo(totalPrice);
        }

        @Nested
        @DisplayName("userId가")
        class UserId {
            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void whenUserIdIsNullThrowsBadRequestException() {
                // arrange
                Long userId = null;

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    OrderFixture.createOrderWithUserId(userId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("totalPrice가")
        class TotalPrice {

            @ParameterizedTest
            @DisplayName("없거나 0 이하이면, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(longs = {
                    0L, -1L, -100L
            })
            @NullSource
                // null 값도 테스트에 포함
            void whenTotalPriceIsZeroOrNegativeThrowsBadRequest(Long totalPrice) {
                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    OrderFixture.createOrderWithTotalPrice(totalPrice);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }
    }
}