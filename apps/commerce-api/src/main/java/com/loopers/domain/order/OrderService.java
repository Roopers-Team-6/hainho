package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderInfo.Create create(OrderCommand.Create command) {
        if (command.items().isEmpty()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 아이템은 비어있을 수 없습니다.");
        }

        Order order = Order.create(command.userId(), calculateTotalPrice(command.items()));
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> items = command.items()
                .stream()
                .map(item -> OrderItem.create(savedOrder.getId(), item.productId(), item.quantity(), item.price()))
                .map(orderItemRepository::save)
                .toList();

        return OrderInfo.Create.from(savedOrder, items);
    }

    private Long calculateTotalPrice(List<OrderCommand.Create.OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(item -> item.quantity() * item.price())
                .sum();
    }

    @Transactional
    public Long applyDiscount(Long orderId, Long discountingAmount) {
        Order order = getOrder(orderId);
        order.applyDiscount(discountingAmount);
        return order.getDiscountedPrice().getValue();
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다. ID: " + orderId));
    }

    @Transactional
    public OrderInfo.Detail verifyPayableAndMarkProcessing(Long orderId, Long userId) {
        Order order = getOrder(orderId, userId);
        order.markProcessing();
        return OrderInfo.Detail.from(order);
    }

    private Order getOrder(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다. ID: " + orderId));
    }

    @Transactional
    public OrderInfo.Detail markCompleted(Long orderId) {
        Order order = getOrder(orderId);
        order.markCompleted();
        return OrderInfo.Detail.from(order);
    }

    public OrderInfo.PendingOrders findOldPendingOrders() {
        List<Order> pendingOrders = orderRepository.findAllByStatusAndUpdatedAtBefore(
                OrderStatus.PENDING,
                ZonedDateTime.now().minusMinutes(10)
        );
        return OrderInfo.PendingOrders.from(pendingOrders);
    }

    public List<OrderItemInfo.Detail> findOrderItems(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId).stream()
                .map(OrderItemInfo.Detail::from)
                .toList();
    }

    @Transactional
    public void markCancelled(Long orderId) {
        Order order = getOrder(orderId);
        order.markCancelled();
    }

    @Transactional
    public OrderInfo.Detail markPending(Long orderId) {
        Order order = getOrder(orderId);
        order.markPending();
        return OrderInfo.Detail.from(order);
    }
}
