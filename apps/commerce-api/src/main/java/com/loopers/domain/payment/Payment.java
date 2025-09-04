package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Table(name = "payments")
public class Payment extends BaseEntity {
    private Long orderId;
    @Column(unique = true)
    private String transactionKey;
    @Enumerated
    private PaymentMethod paymentMethod;
    @Embedded
    private PaymentAmount amount;
    @Enumerated
    private PaymentStatus status;

    private Payment(Long orderId, PaymentMethod paymentMethod, PaymentAmount amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public static Payment create(PaymentCommand.Create command) {
        if (command.orderId() == null) {
            throw new IllegalArgumentException("주문 ID는 null일 수 없습니다.");
        }
        return new Payment(
                command.orderId(),
                PaymentMethod.from(command.paymentMethod()),
                PaymentAmount.of(command.amount()));
    }

    public void markCompleted(String transactionKey) {
        if (transactionKey == null) {
            throw new IllegalArgumentException("트랜잭션 키는 null일 수 없습니다.");
        }
        if (!status.isCompletable()) {
            throw new IllegalStateException("결제 상태가 완료 가능한 상태가 아닙니다. 현재 상태: " + status);
        }
        this.transactionKey = transactionKey;
        this.status = PaymentStatus.COMPLETED;
    }

    public void completed() {
        if (!status.isCompletable()) {
            throw new IllegalStateException("결제 상태가 완료 가능한 상태가 아닙니다. 현재 상태: " + status);
        }
        this.status = PaymentStatus.COMPLETED;
    }

    public void markFailed() {
        if (!status.isFailable()) {
            throw new IllegalStateException("결제 상태가 실패 가능한 상태가 아닙니다. 현재 상태: " + status);
        }
        this.status = PaymentStatus.FAILED;
    }

    public void markRequested(String transactionKey) {
        if (transactionKey == null) {
            throw new IllegalArgumentException("트랜잭션 키는 null일 수 없습니다.");
        }
        if (!status.isRequestable()) {
            throw new IllegalStateException("결제 상태가 요청 가능한 상태가 아닙니다. 현재 상태: " + status);
        }
        this.transactionKey = transactionKey;
        this.status = PaymentStatus.REQUESTED;
    }

    public void markRequested() {
        if (!status.isRequestable()) {
            throw new IllegalStateException("결제 상태가 요청 가능한 상태가 아닙니다. 현재 상태: " + status);
        }
        this.status = PaymentStatus.REQUESTED;
    }
}
