package com.loopers.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderInfo {

    @Getter
    public static class Create {
        private final Long id;
        private final Long totalPrice;
        private final List<OrderItemInfo.Create> items;

        private Create(Long id, Long totalPrice, List<OrderItemInfo.Create> items) {
            this.id = id;
            this.totalPrice = totalPrice;
            this.items = items;
        }

        public static OrderInfo.Create from(Order order, List<OrderItem> items) {
            List<OrderItemInfo.Create> itemInfos = items.stream()
                    .map(OrderItemInfo.Create::from)
                    .toList();
            return new OrderInfo.Create(order.getId(), order.getTotalPrice().getValue(), itemInfos);
        }
    }
}
