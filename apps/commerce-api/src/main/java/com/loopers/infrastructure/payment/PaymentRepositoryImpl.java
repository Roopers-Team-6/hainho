package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByIdWithLock(Long id) {
        return paymentJpaRepository.findByIdWithLock(id);
    }

    @Override
    public Optional<Payment> findByTransactionKeyWithLock(String transactionKey) {
        return paymentJpaRepository.findByTransactionKeyWithLock(transactionKey);
    }
}
