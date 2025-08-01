package com.loopers.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> find(Long productId);

    List<ProductInfo.GetPage> getPage(Long userId, Long brandId, String sort, Long page, Long size);
}
