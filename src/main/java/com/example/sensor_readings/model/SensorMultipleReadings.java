package com.example.sensor_readings.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SensorMultipleReadings {

    @NotNull
    private String sensorId;

    @NotNull
    @Size(min = 1)
    private List<Reading> readings;

    public SensorMultipleReadings() {
    }

    public SensorMultipleReadings(String sensorId, List<Reading> readings) {
        this.sensorId = sensorId;
        this.readings = readings;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }
}
