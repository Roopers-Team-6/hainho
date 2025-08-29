package com.loopers.domain.coupon;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponService {
    private final CouponIssuanceRepository couponIssuanceRepository;
    private final CouponRepository couponRepository;
    private final CouponEventPublisher couponEventPublisher;

    @Transactional
    public Long useCoupon(Long couponIssuanceId, Long orderId, Long orderPrice) {
        CouponIssuance couponIssuance = getCouponIssuance(couponIssuanceId);

        Long couponId = couponIssuance.getCouponId();
        Coupon coupon = getCoupon(couponId);
        Long discountAmount = coupon.calculateDiscountAmount(orderPrice);

        couponIssuance.use(orderId);


        return discountAmount;
    }

    private CouponIssuance getCouponIssuance(Long couponIssuanceId) {
        return couponIssuanceRepository.findById(couponIssuanceId)
                .orElseThrow(() -> new EntityNotFoundException("쿠폰 발급 정보를 찾을 수 없습니다. 쿠폰 발급 ID: " + couponIssuanceId));
    }

    private Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new EntityNotFoundException("쿠폰 정보를 찾을 수 없습니다. 쿠폰 ID: " + couponId));
    }
}
