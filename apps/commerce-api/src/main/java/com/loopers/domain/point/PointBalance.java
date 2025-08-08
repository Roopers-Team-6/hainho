package com.loopers.domain.point;

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
public class PointBalance {
    public static final PointBalance ZERO = new PointBalance(0L);
    private static final long MIN_BALANCE = 0L;
    private static final long MIN_CHARGE_AMOUNT = 1L;
    private static final long MIN_USE_AMOUNT = 1L;

    @Column(name = "balance")
    private final Long value;

    private PointBalance(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("포인트 잔액은 null일 수 없습니다.");
        }
        if (value < MIN_BALANCE) {
            throw new IllegalArgumentException("포인트 잔액은 " + MIN_BALANCE + "보다 작을 수 없습니다.");
        }
        this.value = value;
    }

    public static PointBalance of(Long balance) {
        return new PointBalance(balance);
    }

    public PointBalance charge(Long amount) {
        if (amount == null) {
            throw new IllegalArgumentException("충전 금액은 null일 수 없습니다.");
        }
        if (amount < MIN_CHARGE_AMOUNT) {
            throw new IllegalArgumentException("충전 금액은 " + MIN_CHARGE_AMOUNT + "보다 커야 합니다.");
        }
        return new PointBalance(this.value + amount);
    }

    public PointBalance use(Long amount) {
        if (amount == null) {
            throw new IllegalArgumentException("사용 금액은 null일 수 없습니다.");
        }
        if (amount < MIN_USE_AMOUNT) {
            throw new IllegalArgumentException("사용 금액은 " + MIN_USE_AMOUNT + "보다 커야 합니다.");
        }
        if (this.value < amount) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }
        return new PointBalance(this.value - amount);
    }
}
