package com.example.sensor_readings.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="SENSOR_READINGS")
public class SensorReading {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @NotNull
    @Column(name="sensor_id",nullable = false)
    private String sensorId;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="timestamp", column=@Column(name="timestamp")),
            @AttributeOverride(name="temperature", column=@Column(name="temperature")),
            @AttributeOverride(name="humidity", column=@Column(name="humidity"))
    })
    private Reading reading;

    public SensorReading() {
    }

    public SensorReading(String sensorId, Reading reading) {
        this.sensorId = sensorId;
        this.reading = reading;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Reading getReading() {
        return reading;
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }
}
