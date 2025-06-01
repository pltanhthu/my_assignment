package com.example.sensor_readings.exception;

import java.time.LocalDateTime;

public class SensorReadingNotFoundException extends RuntimeException {

    public SensorReadingNotFoundException(String sensorId) {
        super("Could not find any reading from sensorId = " + sensorId + " in provided date range");
    }

    public SensorReadingNotFoundException() {
        super("Could not find any reading of any sensor in provided date range");
    }
}
