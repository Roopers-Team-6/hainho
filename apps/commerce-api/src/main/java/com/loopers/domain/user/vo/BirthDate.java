package com.loopers.domain.user.vo;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class BirthDate {
    private final LocalDate birthDate;

    private BirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 비어있을 수 없습니다.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 현재 날짜 이후로 설정할 수 없습니다: " + birthDate);
        }
        this.birthDate = birthDate;
    }

    public static BirthDate of(String birthDate) {
        return new BirthDate(parseBirthDate(birthDate));
    }

    private static LocalDate parseBirthDate(String birthDate) {
        if (birthDate == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 비어있을 수 없습니다.");
        }
        try {
            return LocalDate.parse(birthDate);
        } catch (DateTimeParseException e) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 올바르지 않습니다. " + e.getMessage());
        }
    }
}
