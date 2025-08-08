package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "coupon_issuance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponIssuance extends BaseEntity {
    private Long couponId;
    private Long userId;
    private Long usedOrderId;
    private ZonedDateTime usedAt;

    @Version
    private Long version;

    private CouponIssuance(Long couponId, Long userId) {
        if (couponId == null) {
            throw new IllegalArgumentException("couponId는 null일 수 없습니다.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        this.couponId = couponId;
        this.userId = userId;
    }

    public static CouponIssuance create(Long couponId, Long userId) {
        return new CouponIssuance(couponId, userId);
    }

    public void use(Long usedOrderId) {
        if (isUsed()) {
            throw new IllegalStateException("사용된 쿠폰은 재사용할 수 없습니다.");
        }
        this.usedAt = ZonedDateTime.now();
        if (usedOrderId == null) {
            throw new IllegalArgumentException("usedOrderId는 null일 수 없습니다.");
        }
        this.usedOrderId = usedOrderId;
    }

    public boolean isUsed() {
        return this.usedAt != null;
    }
}
