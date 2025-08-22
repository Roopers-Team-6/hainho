package com.loopers.domain.order;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long id);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findAllByStatusAndUpdatedAtBefore(OrderStatus status, ZonedDateTime updatedAt);
}
