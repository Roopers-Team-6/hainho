package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.CouponIssuance;
import com.loopers.domain.coupon.CouponIssuanceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssuanceRepositoryImpl implements CouponIssuanceRepository {
    private final CouponIssuanceJpaRepository couponIssuanceJpaRepository;

    @Override
    public CouponIssuance save(CouponIssuance couponIssuance) {
        return couponIssuanceJpaRepository.save(couponIssuance);
    }

    @Override
    public Optional<CouponIssuance> findById(Long id) {
        return couponIssuanceJpaRepository.findById(id);
    }

    @Override
    public List<CouponIssuance> findAllByUsedOrderId(Long usedOrderId) {
        return couponIssuanceJpaRepository.findAllByUsedOrderId(usedOrderId);
    }
}
