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

    public record Detail(
            Long id,
            Long userId,
            Long totalPrice,
            Long discountedPrice,
            String status
    ) {
        public static Detail from(Order order) {
            return new Detail(
                    order.getId(),
                    order.getUserId(),
                    order.getTotalPrice().getValue(),
                    order.getDiscountedPrice() != null ? order.getDiscountedPrice().getValue() : null,
                    order.getStatus().name()
            );
        }
    }

    public record PendingOrders(
            List<OrderInfo.Detail> orders
    ) {
        public static PendingOrders from(List<Order> orders) {
            List<OrderInfo.Detail> orderDetails = orders.stream()
                    .map(OrderInfo.Detail::from)
                    .toList();
            return new PendingOrders(orderDetails);
        }
    }
}
