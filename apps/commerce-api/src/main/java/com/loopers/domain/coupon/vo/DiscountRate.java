package com.loopers.domain.coupon.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class DiscountRate {
    private static final BigDecimal MIN_VALUE = BigDecimal.valueOf(0.01); // 1%
    private static final BigDecimal MAX_VALUE = BigDecimal.valueOf(1.00); // 100%
    private static final MathContext MATH_CONTEXT = new MathContext(0, RoundingMode.UP); // 소수점 이하 0자리에서 올림

    private final BigDecimal value;

    private DiscountRate(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("할인율은 null일 수 없습니다.");
        }
        if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
            throw new IllegalArgumentException("할인율은 " + MIN_VALUE + " 이상 " + MAX_VALUE + " 이하이어야 합니다.");
        }
        this.value = value;
    }

    public static DiscountRate of(BigDecimal value) {
        return new DiscountRate(value);
    }

    public Long calculateDiscountAmount(Long orderPrice) {
        BigDecimal decimalOrderPrice = BigDecimal.valueOf(orderPrice);
        BigDecimal discountAmount = decimalOrderPrice.multiply(value, MATH_CONTEXT);
        return discountAmount.longValue();
    }
}
