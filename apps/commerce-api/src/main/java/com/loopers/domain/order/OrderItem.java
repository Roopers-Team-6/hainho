package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.order.vo.ItemPrice;
import com.loopers.domain.order.vo.ItemQuantity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseEntity {
    private Long orderId;
    private Long productId;
    @Embedded
    private ItemQuantity quantity;
    @Embedded
    private ItemPrice price;

    private OrderItem(Long orderId, Long productId, ItemQuantity quantity, ItemPrice price) {
        if (orderId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "orderId는 null일 수 없습니다.");
        }
        this.orderId = orderId;
        if (productId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "productId는 null일 수 없습니다.");
        }
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem create(Long orderId, Long productId, Long quantity, Long price) {
        return new OrderItem(orderId, productId, ItemQuantity.of(quantity), ItemPrice.of(price));
    }
}
