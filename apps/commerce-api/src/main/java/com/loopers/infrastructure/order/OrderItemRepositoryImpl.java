package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemRepositoryImpl implements OrderItemRepository {
    private final OrderItemJpaRepository orderItemJpaRepository;

    public OrderItem save(OrderItem orderItem) {
        return orderItemJpaRepository.save(orderItem);
    }
}
