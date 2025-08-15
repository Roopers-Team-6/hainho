package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductResult;
import com.loopers.interfaces.api.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductV1Controller implements ProductV1ApiSepc {

    private final ProductFacade productFacade;

    @Override
    @GetMapping("/products")
    public ApiResponse<ProductV1Dto.GetProducts.Response> getProducts(
            ProductV1Dto.GetProducts.Request request,
            Pageable pageable,
            @RequestHeader(value = "X-USER-ID", required = false) Long userId
    ) {
        ProductResult.Get.Page page = productFacade.getProductPage(userId, request.brandId(), pageable);
        ProductV1Dto.GetProducts.Response response = ProductV1Dto.GetProducts.Response.from(page, pageable);
        return ApiResponse.success(response);
    }
}
