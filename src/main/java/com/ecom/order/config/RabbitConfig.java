package com.ecom.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitConfig {

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("ecom.order");
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue("ecom.notification.order", true);
    }

    @Bean
    public Binding notificationBinding(TopicExchange orderExchange, Queue notificationQueue) {
        return BindingBuilder.bind(notificationQueue).to(orderExchange).with("order.#");
    }
}
