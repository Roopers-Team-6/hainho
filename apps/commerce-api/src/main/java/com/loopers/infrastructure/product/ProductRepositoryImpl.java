package com.loopers.infrastructure.product;

import com.loopers.domain.product.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.loopers.domain.brand.QBrand.brand;
import static com.loopers.domain.like.QLikeProduct.likeProduct;
import static com.loopers.domain.like.QLikeProductCount.likeProductCount;
import static com.loopers.domain.product.QProduct.product;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Product> find(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public List<ProductInfo.GetPage> getPage(Long userId, Long brandId, String sort, Long page, Long size) {
        JPAQuery<ProductInfo.GetPage> query = queryFactory.select(
                        new QProductInfo_GetPage(
                                product.id,
                                product.name.value,
                                product.price.value,
                                new QProductInfo_GetPage_Brand(
                                        brand.id,
                                        brand.name.value
                                ),
                                new QProductInfo_GetPage_Like(
                                        likeProductCount.countValue.value,
                                        likeProduct.id.isNotNull()
                                )
                        ))
                .from(product)
                .leftJoin(brand).on(brand.id.eq(product.brandId))
                .leftJoin(likeProduct).on(likeProduct.productId.eq(product.id).and(likeProduct.userId.eq(userId)))
                .leftJoin(likeProductCount).on(likeProductCount.productId.eq(product.id))
                .where(product.brandId.eq(brandId));

        switch (sort) {
            case "latest" -> query.orderBy(product.createdAt.desc());
            case "priceAsc" -> query.orderBy(product.price.value.asc());
            case "likes_desc" -> query.orderBy(likeProductCount.countValue.value.desc());
            default -> query.orderBy(product.id.asc());
        }

        List<ProductInfo.GetPage> fetched = query.offset(page * size)
                .limit(size)
                .fetch();

        return fetched;
    }
}