package com.loopers.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointTest {

    @Nested
    @DisplayName("Point 객체를 생성할 때,")
    class CreatePoint {

        @Test
        @DisplayName("userId가 null이면, IllegalArgumentException이 발생한다.")
        void shouldThrowExceptionWhenUserIdIsNull() {
            // arrange
            Long userId = null;

            // act
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
                Point.create(userId);
            });

            // assert
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("userId가 유효하면, balance가 0인 Point 객체가 생성된다.")
        void shouldCreatePointWithZeroBalanceWhenUserIdIsValid() {
            // arrange
            Long userId = 1L;

            // act
            Point point = Point.create(userId);

            // assert
            assertAll(
                    () -> assertThat(point.getUserId()).isEqualTo(userId),
                    () -> assertThat(point.getBalance().getValue()).isZero()
            );
        }
    }

    @Nested
    @DisplayName("Point를 사용할 때,")
    class UsePoint {

        @Test
        @DisplayName("사용하려는 금액이 null이면, IllegalArgumentException이 발생한다.")
        void shouldThrowExceptionWhenUsingNullAmount() {
            // arrange
            Point point = Point.create(1L);
            Long amountToUse = null;

            // act
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
                point.use(amountToUse);
            });

            // assert
            assertThat(result).isNotNull();
        }


        @ParameterizedTest
        @DisplayName("사용하려는 금액이 0이하면, IllegalArgumentException이 발생한다.")
        @ValueSource(longs = {0L, -1L, -100L})
        void shouldThrowExceptionWhenUsingZeroOrNegativeAmount(long amountToUse) {
            // arrange
            Point point = Point.create(1L);

            // act
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
                point.use(amountToUse);
            });

            // assert
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("잔액보다 큰 금액을 사용하려고 하면, IllegalArgumentException이 발생한다.")
        void shouldThrowExceptionWhenUsingMoreThanBalance() {
            // arrange
            Point point = Point.create(1L);
            Long balance = 100L;
            point.charge(100L);
            Long amountToUse = balance + 1;

            // act
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
                point.use(amountToUse);
            });

            // assert
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("잔액보다 적은 금액을 사용하면, 잔액이 감소한다.")
        void shouldDecreaseBalanceWhenUsingLessThanBalance() {
            // arrange
            Point point = Point.create(1L);
            Long initialBalance = 100L;
            point.charge(initialBalance);
            Long amountToUse = 50L;
            Long expectedBalance = initialBalance - amountToUse;

            // act
            point.use(amountToUse);

            // assert
            assertThat(point.getBalance().getValue()).isEqualTo(expectedBalance);
        }
    }

    @Nested
    @DisplayName("Point를 충전할 때,")
    class ChargePoint {

        @Test
        @DisplayName("충전하려는 금액이 null이면, IllegalArgumentException이 발생한다.")
        void shouldThrowExceptionWhenChargingNullAmount() {
            // arrange
            Point point = Point.create(1L);
            Long amountToCharge = null;

            // act
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
                point.charge(amountToCharge);
            });

            // assert
            assertThat(result).isNotNull();
        }

        @ParameterizedTest
        @DisplayName("충전하려는 금액이 0이하면, IllegalArgumentException이 발생한다.")
        @ValueSource(longs = {0L, -1L, -100L})
        void shouldThrowExceptionWhenChargingZeroOrNegativeAmount(long amountToCharge) {
            // arrange
            Point point = Point.create(1L);

            // act
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
                point.charge(amountToCharge);
            });

            // assert
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("충전하려는 금액이 0보다 크면, 잔액이 증가한다.")
        void shouldIncreaseBalanceWhenChargingPositiveAmount() {
            // arrange
            Point point = Point.create(1L);
            Long amountToCharge = 50L;

            // act
            point.charge(amountToCharge);

            // assert
            assertThat(point.getBalance().getValue()).isEqualTo(amountToCharge);
        }
    }
}