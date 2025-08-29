package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentGateway;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentQuery;
import com.loopers.interfaces.api.ApiResponse;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.SocketException;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PgSimulatorPaymentGateway implements PaymentGateway {
    private static final String CALLBACK_URL = "http://localhost:8080/api/v1/payments/callback";
    private static final String PG_X_USER_ID = "loopers-e-commerce";
    private final PgSimulatorCommandClient pgSimulatorCommandClient;
    private final PgSimulatorQueryClient pgSimulatorQueryClient;

    @Override
    @Retry(
            name = PgSimulatorCommandResilienceConfig.RETRY_POLICY_NAME,
            fallbackMethod = "requestCardPaymentFallback"
    )
    @CircuitBreaker(
            name = PgSimulatorCommandResilienceConfig.CIRCUIT_BREAKER_POLICY_NAME
    )
    public PaymentInfo.Card.Result requestCardPayment(PaymentCommand.Card.Payment command) {
        PgSimulatorDto.CardPayment.Create.Request request =
                PgSimulatorDto.CardPayment.Create.Request.from(command, CALLBACK_URL);
        ApiResponse<PgSimulatorDto.CardPayment.Create.Response> apiResponse =
                pgSimulatorCommandClient.requestPayment(request, PG_X_USER_ID);
        PgSimulatorDto.CardPayment.Create.Response response = apiResponse.data();
        return response.toPaymentInfo();
    }

    private PaymentInfo.Card.Result requestCardPaymentFallback(
            PaymentCommand.Card.Payment command,
            Throwable throwable
    ) {
        if (throwable instanceof RetryableException && throwable.getCause() instanceof SocketException) {
            return new PaymentInfo.Card.Result(
                    null,
                    "READ_TIMEOUT",
                    "Payment request is pending due to read timeout: " + throwable.getMessage()
            );
        }
        return new PaymentInfo.Card.Result(
                null,
                "FAILED",
                "Payment request failed: " + throwable.getMessage()
        );
    }

    @Override
    @Retry(
            name = PgSimulatorQueryResilienceConfig.RETRY_POLICY_NAME
    )
    @CircuitBreaker(
            name = PgSimulatorQueryResilienceConfig.CIRCUIT_BREAKER_POLICY_NAME
    )
    public PaymentInfo.Card.Get findCardPaymentResult(PaymentQuery.Card.Payment query) {
        ApiResponse<PgSimulatorDto.CardPayment.Query.Response> responseApiResponse
                = pgSimulatorQueryClient.findCardPaymentResult(query.transactionKey(), PG_X_USER_ID);
        PgSimulatorDto.CardPayment.Query.Response response = responseApiResponse.data();
        return response.toPaymentInfo();
    }

    @Override
    @Retry(
            name = PgSimulatorQueryResilienceConfig.RETRY_POLICY_NAME
    )
    @CircuitBreaker(
            name = PgSimulatorQueryResilienceConfig.CIRCUIT_BREAKER_POLICY_NAME
    )
    public PaymentInfo.Card.Order findCardOrderResult(PaymentQuery.Card.Order query) {
        String orderId = query.orderId().toString();
        ApiResponse<PgSimulatorDto.CardOrder.Query.Response> responseApiResponse
                = pgSimulatorQueryClient.findCardOrderResult(orderId, PG_X_USER_ID);
        PgSimulatorDto.CardOrder.Query.Response response = responseApiResponse.data();
        return response.toPaymentInfo();
    }
}
