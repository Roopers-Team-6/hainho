package com.loopers.domain.coupon;

import com.loopers.domain.coupon.vo.DiscountRate;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("RATE_DISCOUNT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RateDiscountCoupon extends Coupon {

    @Embedded
    private DiscountRate discountRate;

    private RateDiscountCoupon(DiscountRate discountRate, String name) {
        super(name);
        this.discountRate = discountRate;
    }

    public static RateDiscountCoupon create(BigDecimal discountRate, String name) {
        return new RateDiscountCoupon(DiscountRate.of(discountRate), name);
    }

    @Override
    public Long calculateDiscountAmount(Long orderPrice) {
        if (orderPrice == null || orderPrice <= 0) {
            throw new IllegalArgumentException("null이거나 0 이하의 주문 금액은 할인할 수 없습니다.");
        }
        Long discountAmount = discountRate.calculateDiscountAmount(orderPrice);
        return Math.min(orderPrice, discountAmount);
    }
}
