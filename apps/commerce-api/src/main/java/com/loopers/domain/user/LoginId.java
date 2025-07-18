package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record LoginId(String loginId) {
    private static final int MAX_LENGTH = 10;
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public LoginId {
        if (loginId == null || loginId.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "LoginId는 비어있을 수 없습니다.");
        }
        if (loginId.length() > MAX_LENGTH) {
            throw new CoreException(ErrorType.BAD_REQUEST, "LoginId는 10자를 초과할 수 없습니다.");
        }
        if (!PATTERN.matcher(loginId).matches()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "LoginId는 영문자와 숫자만 포함할 수 있습니다.");
        }
    }
}
