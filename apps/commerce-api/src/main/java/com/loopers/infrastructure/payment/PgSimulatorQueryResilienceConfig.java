package com.loopers.infrastructure.payment;

import com.loopers.support.config.ResiliencePolicyConfigurer;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class PgSimulatorQueryResilienceConfig implements ResiliencePolicyConfigurer {
    public static final String RETRY_POLICY_NAME = "pgSimulatorQueryRetry";
    public static final String CIRCUIT_BREAKER_POLICY_NAME = "pgSimulatorQueryCircuitBreaker";
    private static final int RETRY_ATTEMPTS = 3;
    private static final int RETRY_WAIT_DURATION_MILLIS = 100;

    @Override
    public void configure(RetryRegistry r, CircuitBreakerRegistry c) {
        r.retry(RETRY_POLICY_NAME, retryConfig());
        c.circuitBreaker(CIRCUIT_BREAKER_POLICY_NAME, circuitBreakerConfig());
    }

    private RetryConfig retryConfig() {
        IntervalFunction intervalFunction =
                IntervalFunction.ofExponentialRandomBackoff(
                        Duration.ofMillis(RETRY_WAIT_DURATION_MILLIS), // 초기 지연
                        2.0,      // 지수 승수 (예: 2배씩)
                        0.3       // 지터 비율 (0.0~1.0; 0.3이면 ±30% 랜덤)
                );

        return RetryConfig.custom()
                .maxAttempts(RETRY_ATTEMPTS)
                .intervalFunction(intervalFunction)
                // 우선 순위 가장 높게 처리됨
                .ignoreExceptions(
                        feign.FeignException.BadRequest.class, // 400 응답으로 재시도로 문제를 해결할 수 없는 케이스
                        feign.FeignException.Unauthorized.class // 401 응답으로 재시도로 문제를 해결할 수 없는 케이스
                )
                // 우선 순위 두번째로 처리됨
                .retryExceptions(
                        FeignException.InternalServerError.class, // 503 응답으로 결제 요청이 PG 서버에 도달하지 못한 케이스
                        feign.RetryableException.class // retryOnException으로 넘겨서 unwrap 한 뒤 분류하여 처리
                )
                .build();
    }

    private CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(20)
                .minimumNumberOfCalls(10)
                .failureRateThreshold(50f)
                .slowCallRateThreshold(50f)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .recordExceptions(feign.RetryableException.class,
                        feign.FeignException.InternalServerError.class,
                        java.net.SocketTimeoutException.class)
                .ignoreExceptions(feign.FeignException.BadRequest.class,
                        feign.FeignException.Unauthorized.class
                )
                .build();
    }
}
