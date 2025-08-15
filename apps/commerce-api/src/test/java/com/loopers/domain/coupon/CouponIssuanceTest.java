package com.loopers.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CouponIssuanceTest {

    @Nested
    @DisplayName("쿠폰 발급을 생성할 때")
    class CreateCouponIssuance {

        @Nested
        @DisplayName("couponId가")
        class CouponIdTests {

            @Test
            @DisplayName("null이면, IllegalArgumentException 예외가 발생한다.")
            void throwIllegalArgumentExceptionWhenCouponIdIsNull() {
                // arrange
                Long couponId = null;

                // act & assert
                assertThrows(IllegalArgumentException.class,
                        () -> CouponIssuanceFixture.createCouponIssuanceWithCouponId(couponId)
                );
            }
        }

        @Nested
        @DisplayName("userId가")
        class UserIdTests {

            @Test
            @DisplayName("null이면, IllegalArgumentException 예외가 발생한다.")
            void throwIllegalArgumentExceptionWhenUserIdIsNull() {
                // arrange
                Long userId = null;

                // act & assert
                assertThrows(IllegalArgumentException.class,
                        () -> CouponIssuanceFixture.createCouponIssuanceWithUserId(userId)
                );
            }
        }
    }

    @Nested
    @DisplayName("쿠폰 발급을 사용하려고 할 때")
    class UseCouponIssuance {

        @Test
        @DisplayName("이미 사용된 쿠폰 발급이면, IllegalStateException 예외가 발생한다.")
        void throwIllegalStateExceptionWhenCouponIssuanceIsUsed() {
            // arrange
            CouponIssuance couponIssuance = CouponIssuanceFixture.createCouponIssuance();
            Long usedOrderId = 1L;
            couponIssuance.use(usedOrderId);

            // act & assert
            assertThrows(IllegalStateException.class,
                    () -> couponIssuance.use(usedOrderId)
            );
        }

        @Test
        @DisplayName("usedOrderId가 null이면, IllegalArgumentException 예외가 발생한다.")
        void throwExceptionWhenUsedOrderIdIsNull() {
            // arrange
            CouponIssuance couponIssuance = CouponIssuanceFixture.createCouponIssuance();
            Long usedOrderId = null;

            // act & assert
            assertThrows(IllegalArgumentException.class,
                    () -> couponIssuance.use(usedOrderId)
            );
        }
    }
}