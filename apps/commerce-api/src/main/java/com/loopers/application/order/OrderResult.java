package com.loopers.application.order;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderItemInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderResult {

    public record Order(Long id, Long totalPrice, List<OrderProduct> products) {
        public static Order from(OrderInfo.Create orderInfo) {
            List<OrderProduct> products = orderInfo.getItems()
                    .stream()
                    .map(OrderProduct::from)
                    .toList();
            return new Order(orderInfo.getId(), orderInfo.getTotalPrice(), products);
        }

        public record OrderProduct(Long productId, Long quantity, Long price) {
            public static OrderProduct from(OrderItemInfo.Create orderItemInfo) {
                return new OrderProduct(
                        orderItemInfo.getProductId(),
                        orderItemInfo.getQuantity(),
                        orderItemInfo.getPrice()
                );
            }
        }
    }
}
