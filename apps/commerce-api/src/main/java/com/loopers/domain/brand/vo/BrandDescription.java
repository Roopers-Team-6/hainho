package com.loopers.domain.brand.vo;

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
public class BrandDescription {
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 50;
    private static final String PATTERN = "^[a-zA-Z0-9가-힣\\s]+$";

    @Column(name = "description")
    private final String value;

    private BrandDescription(String value) {
        if (value == null || value.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "description은 null 또는 빈 문자열일 수 없습니다.");
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new CoreException(ErrorType.BAD_REQUEST, "description은 " + MIN_LENGTH + "~" + MAX_LENGTH + "자 이내여야 합니다.");
        }
        if (!value.matches(PATTERN)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "description은 영어, 숫자, 한글(공백 포함 가능)으로만 구성되어야 합니다.");
        }
        this.value = value;
    }

    public static BrandDescription of(String value) {
        return new BrandDescription(value);
    }
}
