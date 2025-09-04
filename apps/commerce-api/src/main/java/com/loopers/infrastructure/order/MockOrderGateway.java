package com.loopers.infrastructure.order;

import com.loopers.application.order.OrderGateway;
import com.loopers.domain.order.OrderCompleted;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class MockOrderGateway implements OrderGateway {

    @Override
    public void sendOrderCompletedInfo(List<OrderCompleted.OrderItem> itemInfos) {
        log.info("상품이 주문되었습니다: {}", itemInfos);
    }
}
