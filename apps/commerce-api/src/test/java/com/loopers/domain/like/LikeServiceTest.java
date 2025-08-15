package com.loopers.domain.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeProductCountRepository likeProductCountRepository;

    @Mock
    private LikeProductRepository likeProductRepository;

    @Nested
    @DisplayName("likeProduct 메서드 호출할 때,")
    class WhenCallingLikeProduct {

        @Test
        @DisplayName("해당 상품 좋아요가 존재하면, 상품 좋아요를 추가하지 않는다.")
        void shouldNotCreateLikeProductWhenAlreadyExists() {
            // arrange
            Long userId = 1L;
            Long productId = 2L;
            when(likeProductRepository.exists(userId, productId)).thenReturn(true);

            // act
            likeService.likeProduct(userId, productId);

            // assert
            verify(likeProductRepository, never()).save(any(LikeProduct.class));
        }

        @Test
        @DisplayName("해당 상품 좋아요가 존재하지 않으면, 상품 좋아요를 추가한다.")
        void shouldCreateLikeProductWhenNotExists() {
            // arrange
            Long userId = 1L;
            Long productId = 2L;
            when(likeProductRepository.exists(userId, productId)).thenReturn(false);

            // act
            likeService.likeProduct(userId, productId);

            // assert
            verify(likeProductRepository).save(argThat(argument ->
                    argument.getUserId().equals(userId) &&
                            argument.getProductId().equals(productId)
            ));
        }
    }

    @Nested
    @DisplayName("likeProductCancel 메서드 호출할 때,")
    class WhenCallingLikeProductCancel {

        @Test
        @DisplayName("해당 상품 좋아요가 존재하면, 상품 좋아요를 삭제한다.")
        void shouldDeleteLikeProductWhenExists() {
            // arrange
            Long userId = 1L;
            Long productId = 2L;

            LikeProduct likeProduct = mock(LikeProduct.class);
            when(likeProductRepository.find(userId, productId)).thenReturn(Optional.of(likeProduct));

            // act
            likeService.likeProductCancel(userId, productId);

            // assert
            verify(likeProductRepository, times(1)).delete(likeProduct);
        }

        @Test
        @DisplayName("해당 상품 좋아요가 존재하지 않으면, 상품 좋아요를 삭제하지 않는다.")
        void shouldNotDeleteLikeProductWhenNotExists() {
            // arrange
            Long userId = 1L;
            Long productId = 2L;
            when(likeProductRepository.find(userId, productId)).thenReturn(Optional.empty());

            // act
            likeService.likeProductCancel(userId, productId);

            // assert
            verify(likeProductRepository, never()).delete(any(LikeProduct.class));
        }
    }

    @Nested
    @DisplayName("increaseLikeProductCount 메서드 호출할 때,")
    class WhenCallingIncreaseLikeProductCount {

        @Test
        @DisplayName("상품 좋아요 존재 여부와 상관없이, increaseLikeProductCount 메서드를 호출한다.")
        void shouldIncreaseLikeProductCountRegardlessOfExistence() {
            // arrange
            Long productId = 1L;
            when(likeProductCountRepository.increaseLikeCount(productId)).thenReturn(1);

            // act
            likeService.increaseLikeProductCount(productId);

            // assert
            verify(likeProductCountRepository, times(1)).increaseLikeCount(productId);
        }
    }

    @Nested
    @DisplayName("decreaseLikeProductCount 메서드 호출할 때,")
    class WhenCallingDecreaseLikeProductCount {

        @Test
        @DisplayName("상품 좋아요 존재 여부와 상관없이, decreaseLikeProductCount 메서드를 호출한다.")
        void shouldDecreaseLikeProductCountRegardlessOfExistence() {
            // arrange
            Long productId = 1L;
            when(likeProductCountRepository.decreaseLikeCount(productId)).thenReturn(1);

            // act
            likeService.decreaseLikeProductCount(productId);

            // assert
            verify(likeProductCountRepository, times(1)).decreaseLikeCount(productId);
        }
    }
}