package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.CouponEventPublisher;
import com.loopers.domain.coupon.CouponUsed;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponApplicationEventPublisher implements CouponEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(CouponUsed event) {
        applicationEventPublisher.publishEvent(event);
    }
}
