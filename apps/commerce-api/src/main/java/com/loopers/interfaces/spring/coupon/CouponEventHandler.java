package com.loopers.interfaces.spring.coupon;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.order.OrderCreated;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponEventHandler {
    private final CouponService couponService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(OrderCreated event) {
        couponService.useCoupon(event.couponId(), event.orderId(), event.totalPrice());
    }
}
