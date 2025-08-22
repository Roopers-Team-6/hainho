package com.loopers.domain.payment;

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
public class PaymentAmount {
    private static final Long MIN_VALUE = 1L;

    @Column(name = "amount")
    private Long value;

    private PaymentAmount(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("금액은 null일 수 없습니다.");
        }
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("금액은 " + MIN_VALUE + " 이상이어야 합니다.");
        }
        this.value = value;
    }

    public static PaymentAmount of(Long value) {
        return new PaymentAmount(value);
    }
}
