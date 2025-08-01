package com.loopers.application.order;

import com.loopers.application.user.UserFacade;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductStockCommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderFacade {
    private final OrderService orderService;
    private final ProductService productService;
    private final UserFacade userFacade;

    public OrderResult.Order order(OrderCriteria.Order criteria) {
        OrderCommand.Create orderCommand = criteria.toOrderCommand();
        OrderInfo.Create orderInfo = orderService.create(orderCommand);

        Long userId = criteria.userId();
        Long totalPrice = orderInfo.getTotalPrice();
        userFacade.usePoint(userId, totalPrice);

        ProductStockCommand.Deduct productStockCommand = criteria.toProductStockCommand();
        productService.deductStock(productStockCommand);

        return OrderResult.Order.from(orderInfo);
    }
}
