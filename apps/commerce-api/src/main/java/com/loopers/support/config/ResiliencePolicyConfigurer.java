package com.loopers.support.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;

public interface ResiliencePolicyConfigurer {
    void configure(RetryRegistry retry, CircuitBreakerRegistry cb);
}
