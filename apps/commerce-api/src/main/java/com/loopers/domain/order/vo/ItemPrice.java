package com.loopers.domain.order.vo;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ItemPrice {
    private static final Long MIN_VALUE = 1L;

    @Column(name = "price")
    private final Long value;

    private ItemPrice(Long value) {
        if (value == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 가격은 null일 수 없습니다.");
        }
        if (value < MIN_VALUE) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 가격은 " + MIN_VALUE + " 이상이어야 합니다.");
        }
        this.value = value;
    }

    public static ItemPrice of(Long value) {
        return new ItemPrice(value);
    }
}
