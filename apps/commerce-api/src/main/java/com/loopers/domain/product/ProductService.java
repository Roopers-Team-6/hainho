package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductService {
    private final ProductStockRepository productStockRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void deductStock(ProductStockCommand.Deduct command) {
        command.products()
                .stream()
                .forEach(product -> deductStock(product));
    }

    private ProductStock deductStock(ProductStockCommand.Deduct.Product product) {
        ProductStock productStock = getProductStock(product.productId());
        productStock.deduct(product.quantityToDeduct());
        return productStock;
    }

    private ProductStock getProductStock(Long productId) {
        return productStockRepository.findByProductId(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품 재고 데이터가 존재하지 않습니다. productId: " + productId));
    }

    @Transactional(readOnly = true)
    public ProductInfo.Get getProductInfo(Long productId) {
        Product product = getProduct(productId);
        return ProductInfo.Get.from(product);
    }

    private Product getProduct(Long productId) {
        return productRepository.find(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "id에 해당하는 상품을 찾을 수 없습니다. productId: " + productId));
    }

    @Transactional(readOnly = true)
    public List<ProductInfo.GetPage> getProductPage(Long userId, Long brandId, String sort, Long page, Long size) {
        return productRepository.getPage(userId, brandId, sort, page, size);
    }
}
