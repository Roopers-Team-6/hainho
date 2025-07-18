package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record Point(long balance) {
    public static final Point ZERO = new Point(0L);

    public Point charge(long amount) {
        if (amount <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트 충전 값은 0보다 커야 합니다.");
        }
        return new Point(this.balance + amount);
    }
}