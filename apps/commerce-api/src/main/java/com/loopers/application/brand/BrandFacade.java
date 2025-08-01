package com.loopers.application.brand;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandFacade {
    private final BrandService brandService;

    public BrandResult.Get.Detail getBrandDetail(Long brandId) {
        BrandInfo.Get brandInfo = brandService.getBrandInfo(brandId);
        return BrandResult.Get.Detail.from(brandInfo);
    }
}
