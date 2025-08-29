package com.loopers.domain.payment;

public enum PaymentStatus {
    PENDING,
    REQUESTED,
    COMPLETED,
    FAILED;

    public boolean isCompletable() {
        return this == PENDING || this == REQUESTED;
    }

    public boolean isFailable() {
        return this == PENDING || this == REQUESTED;
    }

    public boolean isRequestable() {
        return this == PENDING;
    }
}
