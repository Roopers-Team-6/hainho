package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderResult;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderV1Controller implements OrderV1ApiSpec {

    private final OrderFacade orderFacade;

    @Override
    @PostMapping("/orders")
    public ApiResponse<OrderV1Dto.CreateOrder.Response> createOrder(
            @RequestHeader(value = "X-USER-ID") Long userId,
            @RequestBody @Valid OrderV1Dto.CreateOrder.Request request
    ) {
        OrderCriteria.Order criteria = request.toOrderCriteria(userId);
        OrderResult.Order orderResult = orderFacade.order(criteria);
        OrderV1Dto.CreateOrder.Response response = OrderV1Dto.CreateOrder.Response.from(orderResult);
        return ApiResponse.success(response);
    }
}
