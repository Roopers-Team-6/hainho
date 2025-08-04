package com.loopers.application.brand;

import com.loopers.domain.brand.BrandInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BrandResult {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class Get {

        public record Detail(
                Long id,
                String name,
                String description
        ) {
            public static Detail from(BrandInfo.Get brandInfo) {
                return new Detail(
                        brandInfo.id(),
                        brandInfo.name(),
                        brandInfo.description()
                );
            }
        }
    }
}
