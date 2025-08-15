package com.loopers.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RateDiscountCouponTest {

    @Nested
    @DisplayName("RateDiscountCoupon 생성할 때")
    class CreateRateDiscountCouponTest {

        @Nested
        @DisplayName("할인율이")
        class DiscountRateTest {

            @ParameterizedTest
            @DisplayName("0 이하의 값이 주어지면, IllegalArgumentException 예외가 발생한다.")
            @ValueSource(doubles = {0.0, -0.1, -1.0})
            void throwIllegalArgumentExceptionWhenDiscountRateIsLessThanOrEqualToZero(double rate) {
                // arrange
                BigDecimal discountRate = BigDecimal.valueOf(rate);

                // act
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                    RateDiscountCouponFixture.createRateDiscountCouponWithDiscountRate(discountRate);
                });

                // assert
                assertThat(exception).isNotNull();
            }

            @ParameterizedTest
            @DisplayName("1 초과의 값이 주어지면, IllegalArgumentException 예외가 발생한다.")
            @ValueSource(doubles = {1.1, 2.0, 100.0})
            void throwIllegalArgumentExceptionWhenDiscountRateIsGreaterThanOne(double rate) {
                // arrange
                BigDecimal discountRate = BigDecimal.valueOf(rate);

                // act && assert
                assertThrows(IllegalArgumentException.class, () -> {
                    RateDiscountCouponFixture.createRateDiscountCouponWithDiscountRate(discountRate);
                });
            }

            @Test
            @DisplayName("null이 주어지면, IllegalArgumentException 예외가 발생한다.")
            void throwIllegalArgumentExceptionWhenDiscountRateIsNull() {
                // arrange
                BigDecimal discountRate = null;

                // act && assert
                assertThrows(IllegalArgumentException.class, () -> {
                    RateDiscountCouponFixture.createRateDiscountCouponWithDiscountRate(discountRate);
                });
            }
        }

        @Nested
        @DisplayName("이름이")
        class NameTest {

            @ParameterizedTest
            @DisplayName("2~20자 이내의 영어, 숫자, 한글(공백 포함 가능)이 아닌 경우, IllegalArgumentException 예외가 발생한다.")
            @ValueSource(strings = {
                    "  ", // 길이 2의 blank 문자열
                    "a", // 2자 미만
                    "123456789 123456789 1", // 20자 초과
                    "abcde!@#$%^&*()" // 특수문자 포함
            })
            @NullAndEmptySource
                // null 또는 빈 문자열도 테스트
            void throwIllegalArgumentExceptionWhenNameIsInvalid(String name) {
                // arrange

                // act
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                    RateDiscountCouponFixture.createRateDiscountCouponWithName(name);
                });

                // assert
                assertThat(exception).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("정률 쿠폰 할인 금액을 계산할 때")
    class CalculateDiscountAmountTest {

        @Test
        @DisplayName("주문 금액이 null이면, IllegalArgumentException 예외가 발생한다.")
        void throwIllegalArgumentExceptionWhenOrderPriceIsNull() {
            // arrange
            RateDiscountCoupon coupon = RateDiscountCouponFixture.createRateDiscountCoupon();
            Long orderPrice = null;

            // act
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                coupon.calculateDiscountAmount(orderPrice);
            });

            // assert
            assertThat(exception).isNotNull();
        }

        @ParameterizedTest
        @DisplayName("주문 금액이 0 이하이면, IllegalArgumentException 예외가 발생한다.")
        @ValueSource(longs = {0, -1, -100})
        void throwIllegalArgumentExceptionWhenOrderPriceIsZeroOrLess(long orderPrice) {
            // arrange
            RateDiscountCoupon coupon = RateDiscountCouponFixture.createRateDiscountCoupon();

            // act
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                coupon.calculateDiscountAmount(orderPrice);
            });

            // assert
            assertThat(exception).isNotNull();
        }
    }
}