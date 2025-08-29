package com.loopers.domain.order;

public record OldPendingOrderFound(
        Long orderId
) {
    public static OldPendingOrderFound from(Order order) {
        return new OldPendingOrderFound(
                order.getId()
        );
    }
}
