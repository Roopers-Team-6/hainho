package com.loopers.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> find(Long productId);

    Page<ProductInfo.GetPage> getPage(Long userId, Long brandId, Pageable pageable);
}
