package com.loopers.domain.product.vo;

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
public class Price {
    @Column(name = "price")
    private final Long value;

    private Price(Long value) {
        if (value == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "price는 null일 수 없습니다.");
        }
        if (value < 1) {
            throw new CoreException(ErrorType.BAD_REQUEST, "price는 1 미만의 값이 될 수 없습니다.");
        }
        this.value = value;
    }

    public static Price of(Long value) {
        return new Price(value);
    }
}
