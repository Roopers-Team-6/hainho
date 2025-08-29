package com.loopers.application.order;

import com.loopers.domain.order.OrderCompleted;

import java.util.List;

public interface OrderGateway {
    void sendOrderCompletedInfo(List<OrderCompleted.OrderItem> itemInfos);
}
