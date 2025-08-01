package com.loopers.domain.like.vo;

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
public class LikeCountValue {
    private static final Long MIN_VALUE = 0L;
    public static final LikeCountValue INITIAL_VALUE = new LikeCountValue(MIN_VALUE);

    @Column(name = "count")
    private final Long value;

    private LikeCountValue(Long value) {
        if (value == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 수는 null일 수 없습니다.");
        }
        if (value < MIN_VALUE) {
            throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 수는 " + MIN_VALUE + "보다 작을 수 없습니다.");
        }
        this.value = value;
    }

    public static LikeCountValue of(Long value) {
        return new LikeCountValue(value);
    }

    public LikeCountValue increase() {
        return new LikeCountValue(this.value + 1);
    }

    public LikeCountValue decrease() {
        return new LikeCountValue(this.value - 1);
    }
}
