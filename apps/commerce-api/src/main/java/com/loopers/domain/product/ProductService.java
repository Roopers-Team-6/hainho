package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductService {
    private final ProductRedisRepository productRedisRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void deductStock(ProductStockCommand.Deduct command) {
        command.products()
                .stream()
                .forEach(this::deductStock);
    }

    private ProductStock deductStock(ProductStockCommand.Deduct.Product product) {
        ProductStock productStock = getProductStockWithLock(product.productId());
        productStock.deduct(product.quantityToDeduct());
        return productStock;
    }

    private ProductStock getProductStockWithLock(Long productId) {
        return productStockRepository.findByProductIdWithLock(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품 재고 데이터가 존재하지 않습니다. productId: " + productId));
    }

    @Transactional(readOnly = true)
    public ProductInfo.Get getProductInfo(Long productId) {
        Product product = productRedisRepository.find(productId)
                .orElse(getAndCacheProduct(productId));
        return ProductInfo.Get.from(product);
    }

    private Product getAndCacheProduct(Long productId) {
        Product product = productRepository.find(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "id에 해당하는 상품을 찾을 수 없습니다. productId: " + productId));
        productRedisRepository.set(productId, product);
        return product;
    }

    @Transactional(readOnly = true)
    public Page<ProductInfo.GetPage> getProductPage(Long userId, Long brandId, Pageable pageable) {
        return productRepository.getPage(userId, brandId, pageable);
    }
}
