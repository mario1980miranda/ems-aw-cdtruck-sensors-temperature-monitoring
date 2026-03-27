package com.codetruck.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.codetruck.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.codetruck.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.codetruck.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_ALERTING;
import static com.codetruck.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_TEMPERATURE_PROCESS;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE_TEMPERATURE_PROCESS, concurrency = "2-3")
    @SneakyThrows
    public void handleTemperatureProcessing(@Payload TemperatureLogData temperatureLogData,
                       @Headers Map<String, Object> headers) {
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5));
    }

    @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
    @SneakyThrows
    public void handleAlerting(@Payload TemperatureLogData temperatureLogData,
                       @Headers Map<String, Object> headers) {
        log.info("Alerting: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        Thread.sleep(Duration.ofSeconds(5));
    }

}
