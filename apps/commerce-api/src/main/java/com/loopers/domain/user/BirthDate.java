package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Embeddable
public record BirthDate(LocalDate birthDate) {
    public BirthDate {
        if (birthDate == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 비어있을 수 없습니다.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 현재 날짜 이후로 설정할 수 없습니다: " + birthDate);
        }
    }

    public BirthDate(String birthDate) {
        this(parseBirthDate(birthDate));
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
