package com.loopers.domain.product;

import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("상품 재고를 차감할 때,")
    class DeductStock {

        @Test
        @DisplayName("재고가 20개인 상품에 동시에 1개 재고 차감 요청이 10개가 들어오면, 재고는 10개가 차감되어야 한다.")
        void shouldDeductStockCorrectlyWhenMultipleThreadsAccess() {
            // Arrange
            int threadCount = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            Long productId = 1L;
            Long initialStock = 20L;
            ProductStock productStock = ProductStock.create(productId, initialStock);
            productStockRepository.save(productStock);

            Long quantityToDeduct = 1L;
            ProductStockCommand.Deduct deductCommand = new ProductStockCommand.Deduct(List.of(
                    new ProductStockCommand.Deduct.Product(productId, quantityToDeduct)
            ));

            Long expectedStockAfterDeduction = initialStock - (quantityToDeduct * threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        // Act
                        productService.deductStock(deductCommand);
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
            ProductStock updatedProductStock = productStockRepository.findByProductId(productId).get();
            assertThat(updatedProductStock.getQuantity().getValue()).isEqualTo(expectedStockAfterDeduction);
        }

        @Test
        @DisplayName("재고가 5개인 상품에 동시에 1개 재고 차감 요청이 10개가 들어오면, 재고는 5개가 차감되어야 한다.")
        void shouldNotDeductStockBelowZeroWhenMultipleThreadsAccess() {
            // Arrange
            int threadCount = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            Long productId = 2L;
            Long initialStock = 5L;
            ProductStock productStock = ProductStock.create(productId, initialStock);
            productStockRepository.save(productStock);

            Long quantityToDeduct = 1L;
            ProductStockCommand.Deduct deductCommand = new ProductStockCommand.Deduct(List.of(
                    new ProductStockCommand.Deduct.Product(productId, quantityToDeduct)
            ));

            Long expectedStockAfterDeduction = 0L; // 재고는 0 미만으로 차감되지 않아야 함

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        // Act
                        productService.deductStock(deductCommand);
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
            ProductStock updatedProductStock = productStockRepository.findByProductId(productId).get();
            assertThat(updatedProductStock.getQuantity().getValue()).isEqualTo(expectedStockAfterDeduction);
        }
    }
}