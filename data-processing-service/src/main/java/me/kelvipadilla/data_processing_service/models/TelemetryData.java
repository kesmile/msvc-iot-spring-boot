package me.kelvipadilla.data_processing_service.models;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "sensor_data")
@Entity
public class TelemetryData {
    @Id
    @Column(name = "time")
    @JsonProperty("time")
    private Instant time;

    @Column(name = "device_id")
    @JsonProperty("deviceId")
    private String deviceId;

    @Column(name = "temperature")
    @JsonProperty("temperature")
    private double temperature;

    @Column(name = "humidity")  
    @JsonProperty("humidity")
    private double humidity;
}
