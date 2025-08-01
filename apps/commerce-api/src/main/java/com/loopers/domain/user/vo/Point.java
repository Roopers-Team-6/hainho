package com.loopers.domain.user.vo;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
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
public class Point {
    public static final Point ZERO = new Point(0L);
    private static final long MIN_BALANCE = 0L;

    @Column(name = "point")
    private final Long balance;

    private Point(Long balance) {
        if (balance == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 null일 수 없습니다.");
        }
        if (balance < MIN_BALANCE) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 " + MIN_BALANCE + "보다 작을 수 없습니다.");
        }
        this.balance = balance;
    }

    public static Point of(Long balance) {
        return new Point(balance);
    }

    public Point charge(long amount) {
        if (amount <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트 충전 값은 0보다 커야 합니다.");
        }
        return new Point(this.balance + amount);
    }

    public Point use(long amount) {
        if (amount <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트 사용 값은 0보다 커야 합니다.");
        }
        if (this.balance < amount) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트가 부족합니다.");
        }
        return new Point(this.balance - amount);
    }
}