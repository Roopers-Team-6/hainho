package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRedisRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandRedisTemplate implements BrandRedisRepository {
    private static final String BRAND_KEY_PREFIX = "brand:v1:";
    private static final Long TTL_MINUTES = 10L;

    private final RedisTemplate<String, Object> redisTemplate;

    private String generateKey(Long brandId) {
        return BRAND_KEY_PREFIX + brandId;
    }

    @Override
    public void set(Long brandId, com.loopers.domain.brand.Brand value) {
        String key = generateKey(brandId);
        redisTemplate.opsForValue().set(key, value, TTL_MINUTES, java.util.concurrent.TimeUnit.MINUTES);
    }

    @Override
    public java.util.Optional<com.loopers.domain.brand.Brand> find(Long brandId) {
        String key = generateKey(brandId);
        Brand brand = (Brand) redisTemplate.opsForValue().get(key);
        return java.util.Optional.ofNullable(brand);
    }

    @Override
    public void delete(Long brandId) {
        String key = generateKey(brandId);
        redisTemplate.delete(key);
    }
}
