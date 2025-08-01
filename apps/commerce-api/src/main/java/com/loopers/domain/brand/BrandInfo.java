package com.loopers.domain.brand;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BrandInfo {
    public record Get(
            Long id,
            String name,
            String description
    ) {
        public static Get from(Brand brand) {
            return new Get(
                    brand.getId(),
                    brand.getName().getValue(),
                    brand.getDescription().getValue()
            );
        }
    }
}
