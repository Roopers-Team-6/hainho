package com.loopers.application.order;

import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class OrderFacade {
    private final OrderService orderService;

    @Transactional
    public OrderResult.Order order(OrderCriteria.Order criteria) {
        OrderInfo.Create orderInfo = createOrder(criteria);
        return OrderResult.Order.from(orderInfo);
    }

    private OrderInfo.Create createOrder(OrderCriteria.Order criteria) {
        OrderCommand.Create orderCommand = criteria.toOrderCommand();
        return orderService.create(orderCommand);
    }

    public void processPendingOrders() {
        OrderInfo.PendingOrders pendingOrderInfos = orderService.findOldPendingOrders();
        if (pendingOrderInfos.orders().isEmpty()) {
            return;
        }
        pendingOrderInfos.orders().stream()
                .forEach(this::processPendingOrder);
    }

    private void processPendingOrder(OrderInfo.Detail orderInfo) {
        try {
            orderService.verifyPayableAndMarkProcessing(orderInfo.id(), orderInfo.userId());
        } catch (OptimisticLockException e) {
            log.warn("PendingOrder 처리 중 주문 상태 변경 실패: 주문이 이미 처리 중이거나 완료되었습니다. orderId: {}", orderInfo.id());
        } catch (EntityNotFoundException e) {
            log.warn("PendingOrder 처리 중 주문을 찾을 수 없습니다. orderId: {}", orderInfo.id());
        }
    }
}
