package com.loopers.domain.order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderCommand {

    public record Create(
            Long userId,
            List<OrderItem> items
    ) {
        public record OrderItem(
                Long productId,
                Long quantity,
                Long price
        ) {
        }
    }
}
