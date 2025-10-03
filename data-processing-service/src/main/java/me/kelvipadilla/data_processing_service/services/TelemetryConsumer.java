package me.kelvipadilla.data_processing_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import me.kelvipadilla.data_processing_service.dtos.TelemetryMessage;
import me.kelvipadilla.data_processing_service.models.TelemetryData;
import me.kelvipadilla.data_processing_service.repositories.TelemetryRepository;

@Service
public class TelemetryConsumer {
    private final TelemetryRepository repository;

    public TelemetryConsumer(TelemetryRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "batchContainerFactory")
    @Transactional
    public void consumeAndSaveBatch(List<TelemetryMessage> messages) {
        var entities = messages.stream()
        .filter(m -> m != null)
        .map(m -> {
            TelemetryData e = new TelemetryData();
            e.setTime(m.time());
            e.setDeviceId(m.deviceId().toString());
            e.setTemperature(m.temperature());
            e.setHumidity(m.humidity());
            return e;
        })
        .collect(Collectors.toList());

        if (!entities.isEmpty()) {
            repository.saveAll(entities);
            System.out.println("Guardados " + entities.size() + " registros.");
        }
    }
}
