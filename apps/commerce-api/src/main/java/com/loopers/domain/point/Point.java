package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point extends BaseEntity {
    private Long userId;

    @Embedded
    private PointBalance balance;

    private Point(Long userId, PointBalance balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public static Point create(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 null일 수 없습니다.");
        }
        return new Point(userId, PointBalance.ZERO);
    }

    public void use(Long amount) {
        this.balance = this.balance.use(amount);
    }

    public void charge(Long amount) {
        this.balance = this.balance.charge(amount);
    }
}
