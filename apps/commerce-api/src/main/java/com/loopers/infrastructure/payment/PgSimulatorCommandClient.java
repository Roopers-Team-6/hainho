package com.loopers.infrastructure.payment;

import com.loopers.interfaces.api.ApiResponse;
import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "pgSimulatorCommandClient",
        url = "http://localhost:8082/api/v1/payments",
        configuration = PgSimulatorCommandClient.Config.class
)
public interface PgSimulatorCommandClient {
    @PostMapping
    ApiResponse<PgSimulatorDto.CardPayment.Create.Response> requestPayment(
            @RequestBody PgSimulatorDto.CardPayment.Create.Request request,
            @RequestHeader("X-USER-ID") String userId
    );

    class Config {
        private static int CONNECT_TIMEOUT_MILLIS = 1000;
        private static int READ_TIMEOUT_MILLIS = 3000;

        @Bean
        public feign.Retryer retryer() {
            return feign.Retryer.NEVER_RETRY;
        }

        @Bean
        public Request.Options options() {
            return new Request.Options(CONNECT_TIMEOUT_MILLIS, READ_TIMEOUT_MILLIS);
        }
    }
}
