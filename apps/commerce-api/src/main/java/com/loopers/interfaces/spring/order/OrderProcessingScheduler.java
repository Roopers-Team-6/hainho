package com.loopers.interfaces.spring.order;

import com.loopers.application.order.OrderFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProcessingScheduler {
    private final OrderFacade orderFacade;

    @Scheduled(cron = "0 */3 * * * *", zone = "Asia/Seoul")
    public void processPendingOrders() {
        orderFacade.processPendingOrders();
    }
}
