package com.loopers.domain.point;

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
class PointServiceIntegrationTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @MockitoBean
    private PointEventPublisher pointEventPublisher;

    @BeforeEach
    void setUp() {
        doNothing().when(pointEventPublisher).publish(isA(PointUsed.class));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("포인트를 사용하려고 할 때,")
    class UsePoint {

        @Test
        @DisplayName("20000 포인트를 가진 한 유저가 동시에 1000 포인트를 10번 사용하면, 최종 포인트는 10000이 되어야 한다.")
        void shouldUsePointCorrectlyWhenMultipleThreadsAccess() {
            // Arrange
            int threadCount = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            Long userId = 1L;
            Long initialPoint = 20000L;
            Point point = Point.create(userId);
            point.charge(initialPoint);
            pointRepository.save(point);

            Long amountToUse = 1000L;
            Long expectedPointAfterUsage = initialPoint - (amountToUse * threadCount);

            Long orderId = 3L;
            Long paymentId = 4L;

            // Act
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        // Act
                        pointService.usePoint(userId, amountToUse, orderId, paymentId);
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

            // Assert
            Point pointAfterUsage = pointRepository.findByUserId(userId).get();
            assertThat(pointAfterUsage.getBalance().getValue()).isEqualTo(expectedPointAfterUsage);
        }

        @Test
        @DisplayName("5000 포인트를 가진 한 유저가 동시에 1000 포인트를 10번 사용하면, 최종 포인트는 0이 되어야 한다.")
        void shouldUsePointCorrectlyWhenExceedingBalance() {
            // Arrange
            int threadCount = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            Long userId = 1L;
            Long initialPoint = 5000L;
            Point point = Point.create(userId);
            point.charge(initialPoint);
            pointRepository.save(point);

            Long amountToUse = 1000L;
            Long expectedPointAfterUsage = 0L;

            Long orderId = 3L;
            Long paymentId = 4L;

            // Act
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        // Act
                        pointService.usePoint(userId, amountToUse, orderId, paymentId);
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

            // Assert
            Point pointAfterUsage = pointRepository.findByUserId(userId).get();
            assertThat(pointAfterUsage.getBalance().getValue()).isEqualTo(expectedPointAfterUsage);
        }
    }
}