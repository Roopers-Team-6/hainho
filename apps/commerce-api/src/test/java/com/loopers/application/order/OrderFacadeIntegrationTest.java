package com.loopers.application.order;

import com.loopers.domain.coupon.*;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointFixture;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.product.*;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrderFacadeIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponIssuanceRepository couponIssuanceRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("주문을 처리할 때,")
    class CreateOrder {

        @Test
        @DisplayName("상품의 재고가 부족하면, 주문이 실패하고 포인트와 재고가 차감되지 않고, 쿠폰도 사용되지 않아야 한다.")
        void shouldNotDeductPointAndStockAndCouponWhenProductStockIsInsufficient() {
            // Arrange
            Long initialStock = 1L;
            Long orderQuantity = 2L;
            Long productPrice = 10000L;
            Long pointBalance = productPrice + 1L;

            // 쿠폰과 쿠폰 발급 설정
            Coupon coupon = AmountDiscountCouponFixture.createAmountDiscountCoupon();
            Coupon savedCoupon = couponRepository.save(coupon);

            Long couponId = savedCoupon.getId();
            CouponIssuance couponIssuance = CouponIssuanceFixture.createCouponIssuanceWithCouponId(couponId);
            CouponIssuance savedCouponIssuance = couponIssuanceRepository.save(couponIssuance);

            Long userId = couponIssuance.getUserId();

            // 포인트를 설정
            Point point = PointFixture.createPointWithUserId(userId);
            point.charge(pointBalance);
            pointRepository.save(point);

            // 상품과 재고를 설정
            Product savedProduct = productRepository.save(ProductFixture.createProductWithPrice(productPrice));
            productStockRepository.save(ProductStockFixture.createProductStock(savedProduct.getId(), initialStock));

            Long couponIssuanceId = savedCouponIssuance.getId();

            OrderCriteria.Order criteria = new OrderCriteria.Order(
                    userId,
                    couponIssuanceId,
                    List.of(
                            new OrderCriteria.Order.OrderProduct(savedProduct.getId(), orderQuantity, productPrice)
                    )
            );

            // Act && Assert
            assertThrows(IllegalArgumentException.class, () -> {
                orderFacade.order(criteria);
            });

            CouponIssuance couponIssuanceAfterOrderFailed = couponIssuanceRepository.findById(couponIssuanceId).get();
            assertThat(couponIssuanceAfterOrderFailed.isUsed()).isFalse();

            Point pointAfterOrderFailed = pointRepository.findByUserId(userId).get();
            assertThat(pointAfterOrderFailed.getBalance().getValue()).isEqualTo(pointBalance);

            ProductStock productStockAfterOrderFailed = productStockRepository.findByProductId(savedProduct.getId()).get();
            assertThat(productStockAfterOrderFailed.getQuantity().getValue()).isEqualTo(initialStock);
        }

        @Test
        @DisplayName("포인트가 부족하면, 주문이 실패하고 포인트와 재고가 차감되지 않고, 쿠폰도 사용되지 않아야 한다.")
        void shouldNotDeductPointAndStockAndCouponWhenPointIsInsufficient() {
            // Arrange
            Long initialStock = 10L;
            Long orderQuantity = 1L;
            Long productPrice = 10000L;
            Long pointBalance = 1000L;

            // 쿠폰과 쿠폰 발급 설정
            Coupon coupon = AmountDiscountCouponFixture.createAmountDiscountCoupon();
            Coupon savedCoupon = couponRepository.save(coupon);

            Long couponId = savedCoupon.getId();
            CouponIssuance couponIssuance = CouponIssuanceFixture.createCouponIssuanceWithCouponId(couponId);
            CouponIssuance savedCouponIssuance = couponIssuanceRepository.save(couponIssuance);

            Long userId = couponIssuance.getUserId();

            // 포인트를 설정
            Point point = PointFixture.createPointWithUserId(userId);
            point.charge(pointBalance);
            pointRepository.save(point);

            // 상품과 재고를 설정
            Product savedProduct = productRepository.save(ProductFixture.createProductWithPrice(productPrice));
            productStockRepository.save(ProductStockFixture.createProductStock(savedProduct.getId(), initialStock));

            Long couponIssuanceId = savedCouponIssuance.getId();

            OrderCriteria.Order criteria = new OrderCriteria.Order(
                    userId,
                    couponIssuanceId,
                    List.of(
                            new OrderCriteria.Order.OrderProduct(savedProduct.getId(), orderQuantity, productPrice)
                    )
            );

            // Act && Assert
            assertThrows(IllegalArgumentException.class, () -> {
                orderFacade.order(criteria);
            });

            CouponIssuance couponIssuanceAfterOrderFailed = couponIssuanceRepository.findById(couponIssuanceId).get();
            assertThat(couponIssuanceAfterOrderFailed.isUsed()).isFalse();

            Point pointAfterOrderFailed = pointRepository.findByUserId(userId).get();
            assertThat(pointAfterOrderFailed.getBalance().getValue()).isEqualTo(pointBalance);

            ProductStock productStockAfterOrderFailed = productStockRepository.findByProductId(savedProduct.getId()).get();
            assertThat(productStockAfterOrderFailed.getQuantity().getValue()).isEqualTo(initialStock);
        }

        @Test
        @DisplayName("발급된 쿠폰이 이미 사용되었다면, 주문이 실패하고 포인트와 재고가 차감되지 않아야 한다.")
        void shouldNotDeductPointAndStockWhenCouponIssuanceIsUsed() {
            // Arrange
            Long initialStock = 10L;
            Long orderQuantity = 1L;
            Long productPrice = 10000L;
            Long pointBalance = productPrice + 1L;

            // 쿠폰과 쿠폰 발급 설정
            Coupon coupon = AmountDiscountCouponFixture.createAmountDiscountCoupon();
            Coupon savedCoupon = couponRepository.save(coupon);

            Long couponId = savedCoupon.getId();
            CouponIssuance couponIssuance = CouponIssuanceFixture.createCouponIssuanceWithCouponId(couponId);
            Long usedOrderId = 999L;
            couponIssuance.use(usedOrderId);
            CouponIssuance savedCouponIssuance = couponIssuanceRepository.save(couponIssuance);

            Long userId = couponIssuance.getUserId();

            // 포인트를 설정
            Point point = PointFixture.createPointWithUserId(userId);
            point.charge(pointBalance);
            pointRepository.save(point);

            // 상품과 재고를 설정
            Product savedProduct = productRepository.save(ProductFixture.createProductWithPrice(productPrice));
            productStockRepository.save(ProductStockFixture.createProductStock(savedProduct.getId(), initialStock));

            Long couponIssuanceId = savedCouponIssuance.getId();

            OrderCriteria.Order criteria = new OrderCriteria.Order(
                    userId,
                    couponIssuanceId,
                    List.of(
                            new OrderCriteria.Order.OrderProduct(savedProduct.getId(), orderQuantity, productPrice)
                    )
            );

            // Act && Assert
            assertThrows(IllegalStateException.class, () -> {
                orderFacade.order(criteria);
            });

            CouponIssuance couponIssuanceAfterOrderFailed = couponIssuanceRepository.findById(couponIssuanceId).get();
            assertThat(couponIssuanceAfterOrderFailed.isUsed()).isTrue();

            Point pointAfterOrderFailed = pointRepository.findByUserId(userId).get();
            assertThat(pointAfterOrderFailed.getBalance().getValue()).isEqualTo(pointBalance);

            ProductStock productStockAfterOrderFailed = productStockRepository.findByProductId(savedProduct.getId()).get();
            assertThat(productStockAfterOrderFailed.getQuantity().getValue()).isEqualTo(initialStock);
        }

        @Test
        @DisplayName("주문이 성공하면, 포인트와 재고가 차감되고, 쿠폰이 사용되고 주문이 생성된다.")
        void shouldDeductPointAndStockWhenOrderIsSuccessful() {
            // Arrange
            Long initialStock = 10L;
            Long orderQuantity = 1L;
            Long productPrice = 10000L;
            Long pointBalance = productPrice + 1L;

            // 쿠폰과 쿠폰 발급 설정
            Coupon coupon = AmountDiscountCouponFixture.createAmountDiscountCoupon();
            Coupon savedCoupon = couponRepository.save(coupon);

            Long couponId = savedCoupon.getId();
            CouponIssuance couponIssuance = CouponIssuanceFixture.createCouponIssuanceWithCouponId(couponId);
            CouponIssuance savedCouponIssuance = couponIssuanceRepository.save(couponIssuance);

            Long userId = couponIssuance.getUserId();

            // 포인트를 설정
            Point point = PointFixture.createPointWithUserId(userId);
            point.charge(pointBalance);
            pointRepository.save(point);

            // 상품과 재고를 설정
            Product savedProduct = productRepository.save(ProductFixture.createProductWithPrice(productPrice));
            productStockRepository.save(ProductStockFixture.createProductStock(savedProduct.getId(), initialStock));

            Long couponIssuanceId = savedCouponIssuance.getId();

            OrderCriteria.Order criteria = new OrderCriteria.Order(
                    userId,
                    couponIssuanceId,
                    List.of(
                            new OrderCriteria.Order.OrderProduct(savedProduct.getId(), orderQuantity, productPrice)
                    )
            );

            // Act
            orderFacade.order(criteria);

            // Assert
            CouponIssuance couponIssuanceAfterOrder = couponIssuanceRepository.findById(couponIssuanceId).get();
            assertThat(couponIssuanceAfterOrder.isUsed()).isTrue();

            Point pointAfterOrder = pointRepository.findByUserId(userId).get();
            assertThat(pointAfterOrder.getBalance().getValue()).isEqualTo(pointBalance - (productPrice - savedCoupon.calculateDiscountAmount(productPrice)));

            ProductStock productStockAfterOrder = productStockRepository.findByProductId(savedProduct.getId()).get();
            assertThat(productStockAfterOrder.getQuantity().getValue()).isEqualTo(initialStock - orderQuantity);
        }
    }
}