package com.loopers.application.order;

import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.product.ProductStockCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderCriteria {

    public record Order(
            Long userId,
            List<OrderProduct> products
    ) {
        public OrderCommand.Create toOrderCommand() {
            List<OrderCommand.Create.OrderItem> orderItems = products.stream()
                    .map(product -> new OrderCommand.Create.OrderItem(
                            product.productId(),
                            product.quantity(),
                            product.price()
                    ))
                    .toList();
            return new OrderCommand.Create(userId, orderItems);
        }

        public ProductStockCommand.Deduct toProductStockCommand() {
            List<ProductStockCommand.Deduct.Product> productItems = products.stream()
                    .map(product -> new ProductStockCommand.Deduct.Product(
                            product.productId(),
                            product.quantity()
                    ))
                    .toList();
            return new ProductStockCommand.Deduct(productItems);
        }

        public record OrderProduct(
                Long productId,
                Long quantity,
                Long price
        ) {
        }
    }
}
