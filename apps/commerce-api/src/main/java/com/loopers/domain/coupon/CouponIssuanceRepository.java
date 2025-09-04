package com.loopers.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface CouponIssuanceRepository {
    CouponIssuance save(CouponIssuance couponIssuance);

    Optional<CouponIssuance> findById(Long id);

    List<CouponIssuance> findAllByUsedOrderId(Long usedOrderId);
}
