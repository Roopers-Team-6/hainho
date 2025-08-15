package com.loopers.application.order;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductStockCommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderFacade {
    private final OrderService orderService;
    private final ProductService productService;
    private final PointService pointService;
    private final CouponService couponService;

    @Transactional
    public OrderResult.Order order(OrderCriteria.Order criteria) {
        OrderInfo.Create orderInfo = createOrder(criteria);
        Long discountingAmount = useCoupon(criteria, orderInfo);
        Long discountedPrice = applyDiscount(orderInfo, discountingAmount);
        usePoint(criteria, discountedPrice);
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

    private void usePoint(OrderCriteria.Order criteria, Long discountedPrice) {
        Long userId = criteria.userId();
        pointService.usePoint(userId, discountedPrice);
    }

    private void deductProductStock(OrderCriteria.Order criteria) {
        ProductStockCommand.Deduct productStockCommand = criteria.toProductStockCommand();
        productService.deductStock(productStockCommand);
    }
}
