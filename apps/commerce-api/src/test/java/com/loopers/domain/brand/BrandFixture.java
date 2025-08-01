package com.loopers.domain.brand;

public class BrandFixture {
    public static final String VALID_NAME = "Sample Brand";
    public static final String VALID_DESCRIPTION = "This is a sample brand description.";

    public static Brand createBrand(String name, String description) {
        return Brand.create(name, description);
    }

    public static Brand createBrand() {
        return createBrand(VALID_NAME, VALID_DESCRIPTION);
    }

    public static Brand createBrandWithName(String name) {
        return createBrand(name, VALID_DESCRIPTION);
    }

    public static Brand createBrandWithDescription(String description) {
        return createBrand(VALID_NAME, description);
    }
}
