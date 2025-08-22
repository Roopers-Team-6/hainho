package com.loopers.support.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ResiliencePolicyRegistrar implements SmartInitializingSingleton {
    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final List<ResiliencePolicyConfigurer> configurers;

    @Override
    public void afterSingletonsInstantiated() {
        for (var c : configurers) c.configure(retryRegistry, circuitBreakerRegistry);
    }
}