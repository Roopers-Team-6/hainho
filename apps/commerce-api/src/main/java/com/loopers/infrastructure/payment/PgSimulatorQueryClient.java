package com.loopers.infrastructure.payment;

import com.loopers.interfaces.api.ApiResponse;
import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "pgSimulatorQueryClient",
        url = "http://localhost:8082/api/v1/payments",
        configuration = PgSimulatorQueryClient.Config.class
)
public interface PgSimulatorQueryClient {
    @GetMapping("/{transactionKey}")
    ApiResponse<PgSimulatorDto.CardPayment.Query.Response> findCardPaymentResult(
            @PathVariable("transactionKey") String transactionKey,
            @RequestHeader("X-USER-ID") String userId
    );

    @GetMapping
    ApiResponse<PgSimulatorDto.CardOrder.Query.Response> findCardOrderResult(
            @RequestParam("orderId") String orderId,
            @RequestHeader("X-USER-ID") String userId
    );

    class Config {
        private static int CONNECT_TIMEOUT_MILLIS = 100;
        private static int READ_TIMEOUT_MILLIS = 200;

        @Bean
        @Primary
        public feign.Retryer retryer() {
            return feign.Retryer.NEVER_RETRY;
        }

        @Bean
        @Primary
        public Request.Options options() {
            return new Request.Options(CONNECT_TIMEOUT_MILLIS, READ_TIMEOUT_MILLIS);
        }
    }
}
