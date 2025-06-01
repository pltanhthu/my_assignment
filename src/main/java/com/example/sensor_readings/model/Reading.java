package com.example.sensor_readings.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Embeddable
public class Reading {

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    double temperature;

    @NotNull
    double humidity;

    public Reading() {
    }

    public Reading(LocalDateTime timestamp, double temperature, double humidity) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
