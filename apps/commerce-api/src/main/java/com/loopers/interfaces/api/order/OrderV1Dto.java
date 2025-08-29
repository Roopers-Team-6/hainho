package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderResult;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderV1Dto {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateOrder {
        public record Request(
                Long couponIssuanceId,
                @NotNull
                @NotEmpty
                List<OrderProduct> orderProducts
        ) {
            public OrderCriteria.Order toOrderCriteria(Long userId) {
                List<OrderCriteria.Order.OrderProduct> products = orderProducts.stream()
                        .map(product -> new OrderCriteria.Order.OrderProduct(
                                product.productId,
                                product.quantity,
                                product.price
                        )).toList();
                return new OrderCriteria.Order(userId, couponIssuanceId, products);
            }

            public record OrderProduct(
                    @NotNull
                    Long productId,
                    @NotNull
                    Long quantity,
                    @NotNull
                    Long price
            ) {
            }
        }

        public record Response(
                Long orderId,
                Long totalPrice,
                List<OrderItem> items
        ) {
            public static Response from(OrderResult.Order order) {
                List<OrderItem> items = order.products().stream()
                        .map(item -> new OrderItem(item.productId(), item.quantity(), item.price()))
                        .toList();
                return new Response(order.id(), order.totalPrice(), items);
            }

            public record OrderItem(
                    Long productId,
                    Long quantity,
                    Long price
            ) {
            }
        }
    }
}
