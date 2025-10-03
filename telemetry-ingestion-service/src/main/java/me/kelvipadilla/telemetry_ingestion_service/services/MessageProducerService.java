package me.kelvipadilla.telemetry_ingestion_service.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MessageProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Mensaje enviado: " + message);
    }
}
