package com.loopers.domain.product;

public class ProductStockFixture {
    private static final Long VALID_PRODUCT_ID = 1L;
    private static final Long VALID_QUANTITY = 10L;

    public static ProductStock createProductStock(Long productId, Long quantity) {
        return ProductStock.create(productId, quantity);
    }

    public static ProductStock createProductStockWithQuantity(Long quantity) {
        return createProductStock(VALID_PRODUCT_ID, quantity);
    }

    public static ProductStock createProductStockWithProductId(Long productId) {
        return createProductStock(productId, VALID_QUANTITY);
    }
}
