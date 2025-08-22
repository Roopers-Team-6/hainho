package com.loopers.domain.payment;


import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findByIdWithLock(Long id);

    Optional<Payment> findByTransactionKeyWithLock(String transactionKey);
}
