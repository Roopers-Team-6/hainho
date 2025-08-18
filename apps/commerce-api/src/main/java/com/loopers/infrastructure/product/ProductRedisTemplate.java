package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRedisRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRedisTemplate implements ProductRedisRepository {
    private static final String PRODUCT_KEY_PREFIX = "product:v1:";
    private static final Long TTL_MINUTES = 10L;

    private final RedisTemplate<String, Object> redisTemplate;

    private String generateKey(Long productId) {
        return PRODUCT_KEY_PREFIX + productId;
    }

    @Override
    public void set(Long productId, Product value) {
        String key = generateKey(productId);
        redisTemplate.opsForValue().set(key, value, TTL_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public Optional<Product> find(Long productId) {
        String key = generateKey(productId);
        Product product = (Product) redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(product);
    }

    @Override
    public void delete(Long productId) {
        String key = generateKey(productId);
        redisTemplate.delete(key);
    }
}
