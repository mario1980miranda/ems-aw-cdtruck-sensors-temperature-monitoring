package com.codetruck.algasensors.temperature.monitoring.api.controller;

import com.codetruck.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.codetruck.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.codetruck.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.codetruck.algasensors.temperature.monitoring.domain.model.SensorId;
import com.codetruck.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public SensorAlertOutput getSensorAlertConfig(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .minTemperature(sensorAlert.getMinTemperature())
                .build();
    }

    @PutMapping
    public SensorAlertOutput updateSensorAlertConfig(@PathVariable TSID sensorId, @RequestBody SensorAlertInput input) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElse(SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .maxTemperature(input.getMaxTemperature())
                        .minTemperature(input.getMinTemperature())
                        .build());
        sensorAlert = sensorAlertRepository.saveAndFlush(sensorAlert);
        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .minTemperature(sensorAlert.getMinTemperature())
                .build();
    }

    @DeleteMapping
    public void deleteSensorAlertConfig(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensorAlertRepository.deleteById(sensorAlert.getId());
    }

}
