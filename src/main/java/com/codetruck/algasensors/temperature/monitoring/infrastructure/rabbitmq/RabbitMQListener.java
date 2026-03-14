package com.codetruck.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.codetruck.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.codetruck.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.codetruck.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = TEMPERATURE_MONITORING_PROCESS_TEMPERATURE_V_1_Q)
    public void handle(@Payload TemperatureLogData temperatureLogData,
                       @Headers Map<String, Object> headers) {
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
    }

}
