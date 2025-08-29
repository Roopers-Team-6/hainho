package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Nested
    @DisplayName("주문을 생성할 때,")
    class CreateOrder {

        @Test
        @DisplayName("주문 아이템이 비어있으면, BAD_REQUEST 예외를 발생시킨다.")
        void shouldThrowBadRequestWhenItemsAreEmpty() {
            // arrange
            OrderCommand.Create command = new OrderCommand.Create(1L, List.of(), 2L);

            // act
            CoreException exception = assertThrows(CoreException.class, () -> orderService.create(command));

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}