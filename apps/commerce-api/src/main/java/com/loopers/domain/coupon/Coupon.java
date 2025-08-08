package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.coupon.vo.CouponName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupon")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "coupon_type",
        discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Coupon extends BaseEntity {
    @Embedded
    private CouponName name;

    protected Coupon(String name) {
        this.name = CouponName.of(name);
    }

    public abstract Long calculateDiscountAmount(Long orderPrice);
}
