package com.loopers.interfaces.spring.product;

import com.loopers.domain.order.OrderCancelled;
import com.loopers.domain.order.OrderCreated;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductStockCommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEventHandler {
    private final ProductService productService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(OrderCreated event) {
        ProductStockCommand.Deduct command = new ProductStockCommand.Deduct(
                event.items().stream().map(item -> new ProductStockCommand.Deduct.Product(item.productId(), item.quantity())).toList()
        );
        productService.deductStock(command);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(OrderCancelled event) {
        ProductStockCommand.Refund command = new ProductStockCommand.Refund(
                event.items().stream().map(item -> new ProductStockCommand.Refund.Product(item.productId(), item.quantity())).toList()
        );
        productService.refundStock(command);
    }
}
