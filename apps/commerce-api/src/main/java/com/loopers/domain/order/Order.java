package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.order.vo.TotalPrice;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
public class Order extends BaseEntity {
    private Long userId;
    @Embedded
    private TotalPrice totalPrice;

    private Order(Long userId, TotalPrice totalPrice) {
        if (userId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "userId는 null일 수 없습니다.");
        }
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public static Order create(Long userId, Long totalPrice) {
        return new Order(userId, TotalPrice.of(totalPrice));
    }
}
