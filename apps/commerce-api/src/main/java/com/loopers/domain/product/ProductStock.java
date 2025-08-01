package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.product.vo.ProductStockQuantity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class ProductStock extends BaseEntity {
    private Long productId;
    @Embedded
    private ProductStockQuantity quantity;

    private ProductStock(Long productId, ProductStockQuantity quantity) {
        if (productId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "productId는 null일 수 없습니다.");
        }
        this.productId = productId;
        this.quantity = quantity;
    }

    public static ProductStock create(Long productId, Long quantity) {
        return new ProductStock(productId, ProductStockQuantity.of(quantity));
    }

    public void deduct(Long quantityToDeduct) {
        this.quantity = this.quantity.deduct(quantityToDeduct);
    }
}
