package com.loopers.domain.product.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ProductStockQuantity {
    private static final Long MIN_VALUE = 0L;
    private static final Long MIN_VALUE_FOR_DEDUCT = 1L;
    private static final Long MAX_VALUE_FOR_REFUND = 1L;

    @Column(name = "quantity")
    private final Long value;

    private ProductStockQuantity(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("재고 수량은 null일 수 없습니다.");
        }
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("재고 수량은 " + MIN_VALUE + "보다 작을 수 없습니다.");
        }
        this.value = value;
    }

    public static ProductStockQuantity of(Long value) {
        return new ProductStockQuantity(value);
    }

    public ProductStockQuantity deduct(Long quantityToDeduct) {
        if (quantityToDeduct == null) {
            throw new IllegalArgumentException("차감할 수량은 null일 수 없습니다.");
        }
        if (quantityToDeduct < MIN_VALUE_FOR_DEDUCT) {
            throw new IllegalArgumentException("차감할 수량은 " + MIN_VALUE_FOR_DEDUCT + "보다 작을 수 없습니다.");
        }
        if (this.value < quantityToDeduct) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        return new ProductStockQuantity(this.value - quantityToDeduct);
    }

    public ProductStockQuantity refund(Long quantityToRefund) {
        if (quantityToRefund == null) {
            throw new IllegalArgumentException("환불할 수량은 null일 수 없습니다.");
        }
        if (quantityToRefund < MAX_VALUE_FOR_REFUND) {
            throw new IllegalArgumentException("환불할 수량은 " + MAX_VALUE_FOR_REFUND + "보다 작을 수 없습니다.");
        }
        return new ProductStockQuantity(this.value + quantityToRefund);
    }
}
