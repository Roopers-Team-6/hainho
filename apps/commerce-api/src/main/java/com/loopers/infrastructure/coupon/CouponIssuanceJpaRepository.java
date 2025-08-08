package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.CouponIssuance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssuanceJpaRepository extends JpaRepository<CouponIssuance, Long> {
}
