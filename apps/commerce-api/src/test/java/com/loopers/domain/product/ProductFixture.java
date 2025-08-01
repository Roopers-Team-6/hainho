package com.loopers.domain.product;

public class ProductFixture {
    public static final Long VALID_BRAND_ID = 1L;
    public static final String VALID_NAME = "Sample Product";
    public static final long VALID_PRICE = 1000L;
    public static final String VALID_DESCRIPTION = "This is a sample product description.";

    public static Product createProduct(Long brandId, Long price, String name, String description) {
        return Product.create(brandId, price, name, description);
    }

    public static Product createProduct() {
        return createProduct(VALID_BRAND_ID, VALID_PRICE, VALID_NAME, VALID_DESCRIPTION);
    }

    public static Product createProductWithBrandId(Long brandId) {
        return createProduct(brandId, VALID_PRICE, VALID_NAME, VALID_DESCRIPTION);
    }

    public static Product createProductWithPrice(Long price) {
        return createProduct(VALID_BRAND_ID, price, VALID_NAME, VALID_DESCRIPTION);
    }

    public static Product createProductWithName(String name) {
        return createProduct(VALID_BRAND_ID, VALID_PRICE, name, VALID_DESCRIPTION);
    }

    public static Product createProductWithDescription(String description) {
        return createProduct(VALID_BRAND_ID, VALID_PRICE, VALID_NAME, description);
    }
}
