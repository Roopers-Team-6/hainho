package com.loopers.domain.coupon;

import com.loopers.domain.coupon.vo.DiscountAmount;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("AMOUNT_DISCOUNT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AmountDiscountCoupon extends Coupon {

    @Embedded
    private DiscountAmount discountAmount;

    private AmountDiscountCoupon(DiscountAmount discountAmount, String name) {
        super(name);
        this.discountAmount = discountAmount;
    }

    public static AmountDiscountCoupon create(Long discountAmount, String name) {
        return new AmountDiscountCoupon(DiscountAmount.of(discountAmount), name);
    }

    @Override
    public Long calculateDiscountAmount(Long orderPrice) {
        if (orderPrice == null || orderPrice <= 0) {
            throw new IllegalArgumentException("null이거나 0 이하의 주문 금액은 할인할 수 없습니다.");
        }
        return Math.min(orderPrice, discountAmount.toLong());
    }
}
