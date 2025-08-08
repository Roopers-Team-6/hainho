package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
}
