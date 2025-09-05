package com.loopers.interfaces.consumer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.loopers.interfaces.consumer.audit.*;

import java.time.ZonedDateTime;

public record KafkaMessage<T>(
        String eventId,
        String eventType,
        ZonedDateTime producedAt,
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "eventType")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = OrderCreated.class, name = "OrderCreated"),
                @JsonSubTypes.Type(value = OrderCompleted.class, name = "OrderCompleted"),
                @JsonSubTypes.Type(value = OrderCancelled.class, name = "OrderCancelled"),
                @JsonSubTypes.Type(value = PaymentSucceed.class, name = "PaymentSucceed"),
                @JsonSubTypes.Type(value = PaymentFailed.class, name = "PaymentFailed"),
                @JsonSubTypes.Type(value = CardPaymentCreated.class, name = "CardPaymentCreated"),
                @JsonSubTypes.Type(value = PointPaymentCreated.class, name = "PointPaymentCreated")
        })
        T payload
) {
}
