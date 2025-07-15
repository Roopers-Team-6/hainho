package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record UserId(String userId) {
    private static final int MAX_LENGTH = 10;
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public UserId {
        if (userId == null || userId.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "UserId는 비어있을 수 없습니다.");
        }
        if (userId.length() > MAX_LENGTH) {
            throw new CoreException(ErrorType.BAD_REQUEST, "UserId는 10자를 초과할 수 없습니다.");
        }
        if (!PATTERN.matcher(userId).matches()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "UserId는 영문자와 숫자만 포함할 수 있습니다.");
        }
    }
}
