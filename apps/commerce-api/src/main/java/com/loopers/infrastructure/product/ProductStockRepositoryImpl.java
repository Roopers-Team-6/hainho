package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductStock;
import com.loopers.domain.product.ProductStockRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockRepositoryImpl implements ProductStockRepository {
    private final ProductStockJpaRepository productStockJpaRepository;

    @Override
    public ProductStock save(ProductStock productStock) {
        return productStockJpaRepository.save(productStock);
    }

    @Override
    public Optional<ProductStock> findByProductId(Long productId) {
        return productStockJpaRepository.findByProductId(productId);
    }

    @Override
    public Optional<ProductStock> findByProductIdWithLock(Long productId) {
        return productStockJpaRepository.findByProductIdWithLock(productId);
    }
}
