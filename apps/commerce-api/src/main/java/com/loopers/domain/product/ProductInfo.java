package com.loopers.domain.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductInfo {

    public record Get(
            Long id,
            Long brandId,
            String name,
            String description,
            Long price
    ) {
        @QueryProjection
        public Get {
        }

        public static Get from(Product product) {
            return new Get(
                    product.getId(),
                    product.getBrandId(),
                    product.getName().getValue(),
                    product.getDescription().getValue(),
                    product.getPrice().getValue()
            );
        }
    }

    public record GetPage(
            Long id,
            String name,
            Long price,
            Brand brand,
            Like like
    ) {
        @QueryProjection
        public GetPage {
        }

        public record Brand(
                Long id,
                String name
        ) {
            @QueryProjection
            public Brand {
            }
        }

        public record Like(
                Long count,
                Boolean isLiked
        ) {
            @QueryProjection
            public Like {
            }
        }
    }
}
