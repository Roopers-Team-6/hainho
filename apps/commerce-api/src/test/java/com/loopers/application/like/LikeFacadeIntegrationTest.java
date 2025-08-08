package com.loopers.application.like;

import com.loopers.domain.like.LikeProduct;
import com.loopers.domain.like.LikeProductCount;
import com.loopers.domain.like.LikeProductCountRepository;
import com.loopers.domain.like.LikeProductRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LikeFacadeIntegrationTest {

    @Autowired
    private LikeFacade likeFacade;

    @Autowired
    private LikeProductRepository likeProductRepository;

    @Autowired
    private LikeProductCountRepository likeProductCountRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("한 상품에 대해 좋아요와 좋아요 취소를 할때,")
    class LikeAndCancelLikeProduct {

        @Test
        @DisplayName("10명의 유저가 하나의 상품에 대해 동시에 좋아요를 누르면, 해당 상품의 좋아요 카운트는 10이 되어야 한다.")
        void shouldLikeProductCorrectlyWhenMultipleUsersLike() {
            // Arrange
            int threadCount = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            Long productId = 1L;
            LikeProductCount likeProductCount = LikeProductCount.create(productId);
            likeProductCountRepository.save(likeProductCount);

            Long expectedLikeCount = likeProductCount.getCountValue().getValue() + threadCount;

            // Act
            for (long i = 1; i <= threadCount; i++) {
                final long userId = i;
                executorService.submit(() -> {
                    try {
                        likeFacade.likeProduct(userId, productId);
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
            LikeProductCount likeProductCountAfterRequest = likeProductCountRepository.find(productId).get();
            assertThat(likeProductCountAfterRequest.getCountValue().getValue()).isEqualTo(expectedLikeCount);
        }

        @Test
        @DisplayName("10명의 유저가 하나의 상품에 대해 동시에 좋아요 취소를 하면, 해당 상품의 좋아요 카운트는 0이 되어야 한다.")
        void shouldCancelLikeProductCorrectlyWhenMultipleUsersCancelLike() {
            // Arrange
            int threadCount = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            Long productId = 1L;
            LikeProductCount likeProductCount = LikeProductCount.create(productId);
            likeProductCountRepository.save(likeProductCount);

            // 먼저 좋아요를 누름
            for (long i = 1; i <= threadCount; i++) {
                final long userId = i;
                likeFacade.likeProduct(userId, productId);
            }

            LikeProductCount likeProductCountAfterCreateLike = likeProductCountRepository.find(productId).get();
            Long likeCountAfterCreateLike = likeProductCountAfterCreateLike.getCountValue().getValue();
            Long expectedLikeCountAfterCancel = likeCountAfterCreateLike - threadCount;

            // Act
            for (long i = 1; i <= threadCount; i++) {
                final long userId = i;
                executorService.submit(() -> {
                    try {
                        likeFacade.cancelLikeProduct(userId, productId);
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
            LikeProductCount likeProductCountAfterRequest = likeProductCountRepository.find(productId).get();
            assertThat(likeProductCountAfterRequest.getCountValue().getValue()).isEqualTo(expectedLikeCountAfterCancel);
        }

        @Test
        @DisplayName("10명의 유저가 하나의 상품에 대해 동시에 좋아요를 누르고 또 다른 10명의 유저가 싫어요를 누르면, 해당 상품의 좋아요 카운트는 이전과 동일해야한다.")
        void shouldLikeAndCancelLikeProductCorrectlyWhenMultipleUsersLikeAndCancel() {
            // Arrange
            int threadCount = 20;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            Long productId = 1L;
            LikeProductCount likeProductCount = LikeProductCount.create(productId);
            likeProductCountRepository.save(likeProductCount);

            for (long i = 1; i <= threadCount / 2; i++) {
                LikeProduct likeProduct = LikeProduct.create(i, productId);
                likeProductRepository.save(likeProduct);
            }

            LikeProductCount likeProductCountAfterCreateLike = likeProductCountRepository.find(productId).get();
            Long expectedLikeCount = likeProductCountAfterCreateLike.getCountValue().getValue();

            // Act
            for (long i = 1; i <= threadCount / 2; i++) {
                final long userId = i;
                executorService.submit(() -> {
                    try {
                        likeFacade.cancelLikeProduct(userId, productId);
                    } catch (Exception e) {
                        System.out.println("실패: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            for (long i = threadCount / 2 + 1; i <= threadCount; i++) {
                final long userId = i;
                executorService.submit(() -> {
                    try {
                        likeFacade.likeProduct(userId, productId);
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
            LikeProductCount likeProductCountAfterRequest = likeProductCountRepository.find(productId).get();
            assertThat(likeProductCountAfterRequest.getCountValue().getValue()).isEqualTo(expectedLikeCount);
        }
    }
}