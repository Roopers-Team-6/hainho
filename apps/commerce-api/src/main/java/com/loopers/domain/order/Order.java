package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.order.vo.DiscountedPrice;
import com.loopers.domain.order.vo.TotalPrice;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
public class Order extends BaseEntity {
    private Long userId;
    @Embedded
    private TotalPrice totalPrice;
    @Embedded
    private DiscountedPrice discountedPrice;

    private Order(Long userId, TotalPrice totalPrice) {
        if (userId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "userId는 null일 수 없습니다.");
        }
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public static Order create(Long userId, Long totalPrice) {
        return new Order(userId, TotalPrice.of(totalPrice));
    }

    public void applyDiscount(Long discountingAmount) {
        if (discountingAmount == null) {
            throw new IllegalArgumentException("할인 금액은 null일 수 없습니다.");
        }
        if (discountingAmount < 0) {
            throw new IllegalArgumentException("할인 금액은 0 이상이어야 합니다.");
        }
        Long discountedPriceValue = totalPrice.getValue() - discountingAmount;
        this.discountedPrice = DiscountedPrice.of(discountedPriceValue);
    }
}
