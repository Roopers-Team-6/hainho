package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.brand.vo.BrandDescription;
import com.loopers.domain.brand.vo.BrandName;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class Brand extends BaseEntity {
    @Embedded
    private BrandName name;
    @Embedded
    private BrandDescription description;

    private Brand(BrandName name, BrandDescription description) {
        this.name = name;
        this.description = description;
    }

    public static Brand create(String name, String description) {
        return new Brand(BrandName.of(name), BrandDescription.of(description));
    }
}
