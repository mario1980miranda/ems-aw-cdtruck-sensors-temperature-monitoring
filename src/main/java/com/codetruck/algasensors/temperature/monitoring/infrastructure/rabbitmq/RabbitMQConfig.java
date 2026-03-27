package com.codetruck.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_TEMPERATURE_PROCESS = "temperature-monitoring.process-temperature.v1.q";
    public static final String QUEUE_ALERTING = "temperature-monitoring.alerting.v1.q";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queueTemperatureProcess() {
        return QueueBuilder.durable(QUEUE_TEMPERATURE_PROCESS).build();
    }

    public Queue queueAlerting() {
        return QueueBuilder.durable(QUEUE_ALERTING).build();
    }

    public FanoutExchange exchange() {
        return ExchangeBuilder.fanoutExchange("temperature-processing.temperature-received.v1.e").build();
    }

    @Bean
    public Binding bindingTemperatureProcess() {
        return BindingBuilder.bind(queueTemperatureProcess()).to(exchange());
    }

    @Bean
    public Binding bindingAlerting() {
        return BindingBuilder.bind(queueAlerting()).to(exchange());
    }

}
