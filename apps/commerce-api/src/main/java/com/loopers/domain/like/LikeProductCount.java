package com.loopers.domain.like;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.like.vo.LikeCountValue;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class LikeProductCount extends BaseEntity {
    private Long productId;
    @Embedded
    private LikeCountValue countValue;

    private LikeProductCount(Long productId, LikeCountValue countValue) {
        if (productId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "productId는 null일 수 없습니다.");
        }
        this.productId = productId;
        this.countValue = countValue;
    }

    public static LikeProductCount create(Long productId) {
        return new LikeProductCount(productId, LikeCountValue.INITIAL_VALUE);
    }

    public void increase() {
        this.countValue = this.countValue.increase();
    }

    public void decrease() {
        this.countValue = this.countValue.decrease();
    }
}
