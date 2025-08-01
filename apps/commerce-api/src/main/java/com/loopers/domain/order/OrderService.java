package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());

        return OrderInfo.Create.from(order, items);
    }

    private Long calculateTotalPrice(List<OrderCommand.Create.OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(item -> item.quantity() * item.price())
                .sum();
    }
}
