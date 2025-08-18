package com.loopers.domain.product;

import java.util.Optional;

public interface ProductRedisRepository {
    void set(Long productId, Product value);

    Optional<Product> find(Long productId);

    void delete(Long productId);
}
