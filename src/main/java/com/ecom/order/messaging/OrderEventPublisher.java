package com.ecom.order.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);
    private static final String EXCHANGE = "ecom.order";

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    public void publish(OrderEvent event) {
        if (rabbitTemplate == null) {
            log.warn("RabbitMQ not available — skipping event: {}", event.getEventType());
            return;
        }
        try {
            rabbitTemplate.convertAndSend(EXCHANGE, "order." + event.getEventType(), event);
            log.info("Event published: {} for order {}", event.getEventType(), event.getOrderId());
        } catch (AmqpException e) {
            log.warn("RabbitMQ unavailable — could not publish event {}: {}", event.getEventType(), e.getMessage());
        }
    }
}
