package com.loopers.domain.brand;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandService {
    private final BrandRedisRepository brandRedisRepository;
    private final BrandRepository brandRepository;

    public BrandInfo.Get getBrandInfo(Long brandId) {
        Brand brand = brandRedisRepository.find(brandId)
                .orElse(getAndCacheBrand(brandId));
        return BrandInfo.Get.from(brand);
    }

    private Brand getAndCacheBrand(Long brandId) {
        Brand brand = brandRepository.find(brandId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "id에 해당하는 브랜드를 찾을 수 없습니다. brandId: " + brandId));
        brandRedisRepository.set(brandId, brand);
        return brand;
    }
}
