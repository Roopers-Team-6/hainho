package com.loopers.domain.payment;


import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findByIdWithLock(Long id);

    Optional<Payment> findByTransactionKeyWithLock(String transactionKey);
}
