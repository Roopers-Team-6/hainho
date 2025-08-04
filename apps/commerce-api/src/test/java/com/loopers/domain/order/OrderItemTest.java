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

class OrderItemTest {

    @Nested
    @DisplayName("OrderItem을 생성할 때,")
    class CreateOrderItem {

        @Nested
        @DisplayName("orderId가")
        class OrderIdTest {

            @Test
            @DisplayName("null이면, BAD_REQUEST 예외가 발생한다.")
            void whenOrderIdIsNullThrowsBadRequest() {
                // arrange
                Long orderId = null;

                // act & assert
                CoreException result = assertThrows(CoreException.class, () -> {
                    OrderItemFixture.createOrderItemWithOrderId(orderId);
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
                Long productId = null;

                // act & assert
                CoreException result = assertThrows(CoreException.class, () -> {
                    OrderItemFixture.createOrderItemWithProductId(productId);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("quantity가")
        class QuantityTest {

            @ParameterizedTest
            @DisplayName("없거나 0 이하이면, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(longs = {
                    0L, -1L, -100L
            })
            @NullSource
                // null 값도 테스트에 포함
            void whenQuantityIsZeroOrNegativeThrowsBadRequest(Long quantity) {
                // act & assert
                CoreException result = assertThrows(CoreException.class, () -> {
                    OrderItemFixture.createOrderItemWithQuantity(quantity);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("price가")
        class PriceTest {

            @ParameterizedTest
            @DisplayName("없거나 0 이하이면, BAD_REQUEST 예외가 발생한다.")
            @ValueSource(longs = {
                    0L, -1L, -100L
            })
            @NullSource
                // null 값도 테스트에 포함
            void whenPriceIsZeroOrNegativeThrowsBadRequest(Long price) {
                // act & assert
                CoreException result = assertThrows(CoreException.class, () -> {
                    OrderItemFixture.createOrderItemWithPrice(price);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
            }
        }

        @Nested
        @DisplayName("valid한 값이 주어지면,")
        class ValidValuesTest {

            @Test
            @DisplayName("OrderItem 객체가 생성되고, orderId, productId, quantity, price가 올바르게 설정된다.")
            void whenValidValuesThenCreatesOrderItem() {
                // arrange
                Long orderId = 1L;
                Long productId = 2L;
                Long quantity = 3L;
                Long price = 1000L;

                // act
                OrderItem orderItem = OrderItemFixture.createOrderItem(orderId, productId, quantity, price);

                // assert
                assertThat(orderItem).isNotNull();
                assertThat(orderItem.getOrderId()).isEqualTo(orderId);
                assertThat(orderItem.getProductId()).isEqualTo(productId);
                assertThat(orderItem.getQuantity().getValue()).isEqualTo(quantity);
                assertThat(orderItem.getPrice().getValue()).isEqualTo(price);
            }
        }
    }
}