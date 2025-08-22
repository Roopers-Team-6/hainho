package com.loopers.domain.order;

public enum OrderStatus {
    PENDING, // 주문 대기 중
    PROCESSING, // 주문 처리 중
    COMPLETED, // 주문 완료
    CANCELLED; // 주문 취소됨

    public boolean isCompletable() {
        return this == PROCESSING;
    }

    public boolean isCancellable() {
        return this == PROCESSING;
    }

    public boolean isProcessable() {
        return this == PENDING;
    }

    public boolean isPendable() {
        return this == PROCESSING;
    }
}
