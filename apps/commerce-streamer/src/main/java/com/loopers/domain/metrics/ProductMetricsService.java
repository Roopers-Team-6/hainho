package com.loopers.domain.metrics;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductMetricsService {
    private final ProductMetricsRepository productMetricsRepository;

    public void accumulateViews(Long productId, int views, ZonedDateTime processedAt) {
        LocalDate measuredDate = processedAt.toLocalDate();
        productMetricsRepository.upsertAndAccumulate(productId, measuredDate, views, 0, 0);
    }

    public void accumulatePurchases(Long productId, long purchases, ZonedDateTime processedAt) {
        LocalDate measuredDate = processedAt.toLocalDate();
        productMetricsRepository.upsertAndAccumulate(productId, measuredDate, 0, purchases, 0);
    }

    public void accumulateLikes(Long productId, int likes, ZonedDateTime processedAt) {
        LocalDate measuredDate = processedAt.toLocalDate();
        productMetricsRepository.upsertAndAccumulate(productId, measuredDate, 0, 0, likes);
    }
}
