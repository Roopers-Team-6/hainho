package com.loopers.application.product;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.like.LikeInfo;
import com.loopers.domain.product.ProductInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductResult {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class Get {

        public record Detail(
                Long id,
                String name,
                String description,
                Long price,
                Brand brand,
                Like like
        ) {
            public static Detail from(ProductInfo.Get productInfo, BrandInfo.Get brandInfo, LikeInfo.Get likeInfo) {
                return new Detail(
                        productInfo.id(),
                        productInfo.name(),
                        productInfo.description(),
                        productInfo.price(),
                        Brand.from(brandInfo),
                        Like.from(likeInfo)
                );
            }

            public record Brand(
                    Long id,
                    String name,
                    String description
            ) {
                public static Brand from(BrandInfo.Get brandInfo) {
                    return new Brand(
                            brandInfo.id(),
                            brandInfo.name(),
                            brandInfo.description()
                    );
                }
            }

            public record Like(
                    Long count,
                    Boolean isLiked
            ) {
                public static Like from(LikeInfo.Get likeInfo) {
                    return new Like(
                            likeInfo.count(),
                            likeInfo.isLiked()
                    );
                }
            }
        }

        public record Page(
                List<Product> products
        ) {
            public static Page from(List<ProductInfo.GetPage> products) {
                return new Page(
                        products.stream()
                                .map(Product::from)
                                .toList()
                );
            }

            public record Product(
                    Long id,
                    String name,
                    Long price,
                    Brand brand,
                    Like like
            ) {
                public static Product from(ProductInfo.GetPage productInfo) {
                    return new Product(
                            productInfo.id(),
                            productInfo.name(),
                            productInfo.price(),
                            Brand.from(productInfo.brand()),
                            Like.from(productInfo.like())
                    );
                }
            }

            public record Brand(
                    Long id,
                    String name
            ) {
                public static Brand from(ProductInfo.GetPage.Brand brand) {
                    return new Brand(
                            brand.id(),
                            brand.name()
                    );
                }
            }

            public record Like(
                    Long count,
                    Boolean isLiked
            ) {
                public static Like from(ProductInfo.GetPage.Like like) {
                    return new Like(
                            like.count(),
                            like.isLiked()
                    );
                }
            }
        }
    }

}
