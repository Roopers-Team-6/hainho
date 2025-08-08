package com.loopers.domain.coupon.vo;

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
public class CouponName {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 20;
    private static final String PATTERN = "^[a-zA-Z0-9가-힣\\s]+$";

    @Column(name = "name")
    private final String value;

    private CouponName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("쿠폰 이름은 null이거나 빈 문자열일 수 없습니다.");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("쿠폰 이름은 " + MIN_LENGTH + "~" + MAX_LENGTH + "자 이내여야 합니다.");
        }
        if (!value.matches(PATTERN)) {
            throw new IllegalArgumentException("쿠폰 이름은 영어, 숫자, 한글(공백 포함 가능)으로만 구성되어야 합니다.");
        }
        this.value = value;
    }

    public static CouponName of(String value) {
        return new CouponName(value);
    }
}
