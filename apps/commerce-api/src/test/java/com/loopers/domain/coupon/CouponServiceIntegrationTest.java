package com.loopers.domain.coupon;


import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
class CouponServiceIntegrationTest {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponIssuanceRepository couponIssuanceRepository;

    @MockitoBean
    private CouponEventPublisher couponEventPublisher;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        doNothing().when(couponEventPublisher).publish(isA(CouponUsed.class));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("하나의 발급된 쿠폰을 사용할 때")
    class UseCoupon {

        @Test
        @DisplayName("한 명의 유저가 동시에 10번 쿠폰을 사용하려고 하면, 1회만 성공하고 나머지는 실패한다.")
        void shouldUseCouponCorrectlyOneWhenMultipleUsersUse() {
            // arrange
            int threadCount = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            Long orderId = 10L;
            Long orderPrice = 10000L;

            // 쿠폰과 쿠폰 발급을 생성
            Coupon coupon = AmountDiscountCouponFixture.createAmountDiscountCoupon();
            Coupon savedCoupon = couponRepository.save(coupon);

            Long couponId = savedCoupon.getId();
            CouponIssuance couponIssuance = CouponIssuanceFixture.createCouponIssuanceWithCouponId(couponId);
            CouponIssuance savedCouponIssuance = couponIssuanceRepository.save(couponIssuance);

            Long couponIssuanceId = savedCouponIssuance.getId();
            Long expectedVersion = savedCouponIssuance.getVersion() + 1;

            // act
            for (long i = 1; i <= threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        couponService.useCoupon(couponIssuanceId, orderId, orderPrice);
                    } catch (Exception e) {
                        System.out.println("실패: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                executorService.shutdown();
            }

            // assert
            CouponIssuance updatedCouponIssuance = couponIssuanceRepository.findById(couponIssuanceId).get();
            assertThat(updatedCouponIssuance.isUsed()).isTrue();
            assertThat(updatedCouponIssuance.getVersion()).isEqualTo(expectedVersion);
        }
    }
}