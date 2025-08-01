package com.loopers.domain.brand;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandInfo.Get getBrandInfo(Long id) {
        Brand brand = getBrand(id);
        return BrandInfo.Get.from(brand);
    }

    private Brand getBrand(Long id) {
        return brandRepository.find(id)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "id에 해당하는 브랜드를 찾을 수 없습니다. brandId: " + id));
    }
}
