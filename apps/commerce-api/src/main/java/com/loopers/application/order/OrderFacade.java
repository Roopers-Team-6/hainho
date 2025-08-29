package com.loopers.application.order;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderItemInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductStockCommand;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class OrderFacade {
    private final OrderService orderService;
    private final ProductService productService;
    private final CouponService couponService;
    private final PaymentService paymentService;

    @Transactional
    public OrderResult.Order order(OrderCriteria.Order criteria) {
        OrderInfo.Create orderInfo = createOrder(criteria);
        Long discountingAmount = useCoupon(criteria, orderInfo);
        Long discountedPrice = applyDiscount(orderInfo, discountingAmount);
        deductProductStock(criteria);
        return OrderResult.Order.from(orderInfo, discountedPrice);
    }

    private OrderInfo.Create createOrder(OrderCriteria.Order criteria) {
        OrderCommand.Create orderCommand = criteria.toOrderCommand();
        return orderService.create(orderCommand);
    }

    private Long useCoupon(OrderCriteria.Order criteria, OrderInfo.Create orderInfo) {
        if (criteria.couponIssuanceId() == null) {
            return 0L;
        }
        Long couponIssuanceId = criteria.couponIssuanceId();
        Long orderId = orderInfo.getId();
        Long orderPrice = orderInfo.getTotalPrice();
        return couponService.useCoupon(couponIssuanceId, orderId, orderPrice);
    }

    private Long applyDiscount(OrderInfo.Create orderInfo, Long discountingAmount) {
        Long orderId = orderInfo.getId();
        return orderService.applyDiscount(orderId, discountingAmount);
    }

    private void deductProductStock(OrderCriteria.Order criteria) {
        ProductStockCommand.Deduct productStockCommand = criteria.toProductStockCommand();
        productService.deductStock(productStockCommand);
    }

    public void processPendingOrders() {
        OrderInfo.PendingOrders pendingOrderInfos = orderService.findOldPendingOrders();
        if (pendingOrderInfos.orders().isEmpty()) {
            return;
        }
        pendingOrderInfos.orders().stream()
                .forEach(orderInfo -> {
                    processPendingOrder(orderInfo);
                });
    }

    private void processPendingOrder(OrderInfo.Detail orderInfo) {
        try {
            orderService.verifyPayableAndMarkProcessing(orderInfo.id(), orderInfo.userId());
        } catch (OptimisticLockException e) {
            log.warn("PendingOrder 처리 중 주문 상태 변경 실패: 주문이 이미 처리 중이거나 완료되었습니다. orderId: {}", orderInfo.id());
        } catch (EntityNotFoundException e) {
            log.warn("PendingOrder 처리 중 주문을 찾을 수 없습니다. orderId: {}", orderInfo.id());
        }
        PaymentInfo.Card.Result paymentResult = paymentService.verifyOrderPaymentResult(orderInfo.id());
        if (paymentResult.status().equals("success")) {
            completeOrder(orderInfo);
        } else if (paymentResult.status().equals("failed")) {
            cancelOrder(orderInfo.id());
        } else {
            log.warn("PendingOrder 처리 중 알 수 없는 결제 상태: {}, orderId: {}", paymentResult.status(), orderInfo.id());
        }
    }

    private void completeOrder(OrderInfo.Detail orderInfo) {
        try {
            orderService.markCompleted(orderInfo.id());
        } catch (IllegalStateException e) {
            log.warn("PendingOrder 처리 중 주문 완료 처리 실패: {}", e.getMessage());
        }
    }

    private void cancelOrder(Long orderId) {
        try {
            orderService.markCancelled(orderId);
        } catch (IllegalStateException e) {
            log.warn("PendingOrder 처리 중 주문 취소 처리 실패: {}", e.getMessage());
        }
        List<OrderItemInfo.Detail> itemInfos = orderService.findOrderItems(orderId);
        ProductStockCommand.Refund command = new ProductStockCommand.Refund(
                itemInfos.stream()
                        .map(item -> new ProductStockCommand.Refund.Product(item.productId(), item.quantity()))
                        .toList()
        );
        productService.refundStock(command);
    }
}
