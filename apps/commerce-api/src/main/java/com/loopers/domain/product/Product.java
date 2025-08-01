package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.product.vo.Price;
import com.loopers.domain.product.vo.ProductDescription;
import com.loopers.domain.product.vo.ProductName;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {
    Long brandId;
    @Embedded
    Price price;
    @Embedded
    ProductName name;
    @Embedded
    ProductDescription description;

    private Product(Long brandId, Price price, ProductName name, ProductDescription description) {
        if (brandId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "brandId는 null일 수 없습니다.");
        }
        this.brandId = brandId;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public static Product create(Long brandId, Long price, String name, String description) {
        return new Product(brandId, Price.of(price), ProductName.of(name), ProductDescription.of(description));
    }
}
