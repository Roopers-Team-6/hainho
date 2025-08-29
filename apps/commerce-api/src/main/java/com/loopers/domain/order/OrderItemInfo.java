package com.loopers.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderItemInfo {

    @Getter
    public static class Create {
        private final Long id;
        private final Long productId;
        private final Long quantity;
        private final Long price;

        private Create(Long id, Long productId, Long quantity, Long price) {
            this.id = id;
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }

        public static OrderItemInfo.Create from(OrderItem orderItem) {
            return new OrderItemInfo.Create(
                    orderItem.getId(),
                    orderItem.getProductId(),
                    orderItem.getQuantity().getValue(),
                    orderItem.getPrice().getValue()
            );
        }
    }

    public record Detail(
            Long id,
            Long productId,
            Long quantity,
            Long price
    ) {
        public static Detail from(OrderItem orderItem) {
            return new Detail(
                    orderItem.getId(),
                    orderItem.getProductId(),
                    orderItem.getQuantity().getValue(),
                    orderItem.getPrice().getValue()
            );
        }
    }
}
