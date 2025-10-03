package me.kelvipadilla.data_processing_service;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import org.apache.kafka.common.errors.SerializationException;

import me.kelvipadilla.data_processing_service.dtos.TelemetryMessage;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public DefaultKafkaConsumerFactory<String, TelemetryMessage> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 1) JsonDeserializer estándar
        JsonDeserializer<TelemetryMessage> jsonDeserializer =
            new JsonDeserializer<>(TelemetryMessage.class);

        jsonDeserializer.addTrustedPackages(
            "me.kelvipadilla.data_processing_service.dtos",
            "me.kelvipadilla.telemetry_ingestion_service.dtos");
        jsonDeserializer.setRemoveTypeHeaders(false);

        // 2) Envuelve en ErrorHandlingDeserializer
        ErrorHandlingDeserializer<TelemetryMessage> errorHandlingDeserializer =
            new ErrorHandlingDeserializer<>(jsonDeserializer);

        // 3) Factory que usa StringDeserializer para la clave
        //    y ErrorHandlingDeserializer para el valor
        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            errorHandlingDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TelemetryMessage> batchContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TelemetryMessage> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
            (record, ex) -> {
                System.err.printf("Skipping bad record (offset %d): %s%n",
                    record.offset(), ex.getMessage());
            },
            new FixedBackOff(0L, 0L)
        );
        // Le decimos que NO reintente con SerializationException
        errorHandler.addNotRetryableExceptions(SerializationException.class);

        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}