package com.loopers.domain.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductStockCommand {

    public record Deduct(
            List<Product> products
    ) {
        public record Product(
                Long productId,
                Long quantityToDeduct
        ) {
        }
    }

    public record Refund(
            List<Product> products
    ) {
        public record Product(
                Long productId,
                Long quantityToRefund
        ) {
        }
    }
}
