package com.loopers.domain.payment;

public enum PaymentMethod {
    CARD,
    POINT;

    public static PaymentMethod from(String method) {
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("결제 방법은 null이거나 빈 문자열일 수 없습니다.");
        }
        try {
            return PaymentMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 결제 방법입니다: " + method, e);
        }
    }
}
