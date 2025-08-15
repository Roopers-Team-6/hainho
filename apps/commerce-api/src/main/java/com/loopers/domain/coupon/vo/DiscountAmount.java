package com.loopers.domain.coupon.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class DiscountAmount {
    private static final int MIN_VALUE = 1;

    @Column(name = "discount_value")
    private final BigDecimal value;

    private DiscountAmount(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("할인 금액은 null일 수 없습니다.");
        }
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("할인 금액은 " + MIN_VALUE + " 이상이어야 합니다.");
        }
        this.value = BigDecimal.valueOf(value);
    }

    public static DiscountAmount of(Long value) {
        return new DiscountAmount(value);
    }

    public Long toLong() {
        return value.longValue();
    }
}
