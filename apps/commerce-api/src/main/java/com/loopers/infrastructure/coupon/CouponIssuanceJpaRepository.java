package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.CouponIssuance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponIssuanceJpaRepository extends JpaRepository<CouponIssuance, Long> {
    List<CouponIssuance> findAllByUsedOrderId(Long usedOrderId);
}
