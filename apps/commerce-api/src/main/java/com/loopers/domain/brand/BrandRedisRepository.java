package com.loopers.domain.brand;

import java.util.Optional;

public interface BrandRedisRepository {
    void set(Long brandId, Brand value);

    Optional<Brand> find(Long brandId);

    void delete(Long brandId);
}
