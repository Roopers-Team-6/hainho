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
public class PgSimulatorCommandResilienceConfig implements ResiliencePolicyConfigurer {
    public static final String RETRY_POLICY_NAME = "pgSimulatorCommandRetry";
    public static final String CIRCUIT_BREAKER_POLICY_NAME = "pgSimulatorCommandCircuitBreaker";
    private static final int RETRY_ATTEMPTS = 3;
    private static final int RETRY_WAIT_DURATION_MILLIS = 1000;

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
                        feign.FeignException.Unauthorized.class, // 401 응답으로 재시도로 문제를 해결할 수 없는 케이스
                        java.net.SocketTimeoutException.class // READ_TIMEOUT_MILLIS 초과로 결제 요청이 PG 서버에서 처리 중일 가능성이 있어 재시도 시 중복 결제 우려가 있는 케이스
                )
                // 우선 순위 두번째로 처리됨
                .retryExceptions(
                        FeignException.InternalServerError.class, // 503 응답으로 결제 요청이 PG 서버에 도달하지 못한 케이스
                        feign.RetryableException.class, // retryOnException으로 넘겨서 unwrap 한 뒤 분류하여 처리
                        java.net.ConnectException.class // 커넥션 실패로 결제 요청이 PG 서버에 도달하지 못한 케이스
                )
                // 우선 순위 세번째로 처리됨
                .retryOnException(exception -> {
                            if (exception instanceof FeignException.InternalServerError) { // 503 응답으로 결제 요청이 PG 서버에 도달하지 못한 케이스
                                return true;
                            }
                            if (exception instanceof feign.RetryableException) {
                                switch (exception.getCause()) {
                                    case java.net.ConnectException connectException: // 커넥션 실패로 결제 요청이 PG 서버에 도달하지 못한 케이스
                                        return true;
                                    case java.net.SocketTimeoutException socketTimeoutException: // READ_TIMEOUT_MILLIS 초과로 결제 요청이 PG 서버에서 처리 중일 가능성이 있어 재시도 시 중복 결제 우려가 있는 케이스
                                        return false;
                                    default:
                                        return false;
                                }
                            }
                            return false;
                        }
                )
                .build();
    }

    private CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .slidingWindowSize(60)
                .minimumNumberOfCalls(10)
                .failureRateThreshold(50f)
                .slowCallRateThreshold(50f)
                .slowCallDurationThreshold(Duration.ofMillis(500))
                .waitDurationInOpenState(Duration.ofMillis(10000))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .permittedNumberOfCallsInHalfOpenState(5)
                .maxWaitDurationInHalfOpenState(Duration.ofMillis(10000))
                .recordExceptions(
                        feign.RetryableException.class,
                        feign.FeignException.InternalServerError.class
                )
                .ignoreExceptions(
                        feign.FeignException.BadRequest.class,
                        feign.FeignException.Unauthorized.class
                )
                .build();
    }
}
