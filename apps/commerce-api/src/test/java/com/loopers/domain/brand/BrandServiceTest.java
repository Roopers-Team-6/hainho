package com.loopers.domain.brand;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandRedisRepository brandRedisRepository;


    @Nested
    @DisplayName("getBrandInfo 메서드 호출할 때,")
    class WhenCallingGetBrandInfo {

        @Test
        @DisplayName("존재하지 않는 브랜드 ID를 전달하면, NOT_FOUND 예외가 발생한다.")
        void shouldThrowNotFoundExceptionWhenBrandIdDoesNotExist() {
            // arrange
            Long nonExistentBrandId = 999L;
            when(brandRedisRepository.find(nonExistentBrandId)).thenReturn(Optional.empty());
            when(brandRepository.find(nonExistentBrandId)).thenReturn(Optional.empty());

            // act
            CoreException result = assertThrows(CoreException.class,
                    () -> brandService.getBrandInfo(nonExistentBrandId));

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}