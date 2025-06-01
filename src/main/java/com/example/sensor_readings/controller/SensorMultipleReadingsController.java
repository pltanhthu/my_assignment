package com.example.sensor_readings.controller;

import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.model.SensorMultipleReadings;
import com.example.sensor_readings.service.SensorMultipleReadingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sensor_multiple_readings")
public class SensorMultipleReadingsController {

    @Autowired
    private SensorMultipleReadingsService service;

    @Tag(name = "post", description = "Register multiple readings of a sensor")
    @Operation(summary = "Register multiple readings of a sensor",
            description = "Register multiple readings of a sensor")
    @PostMapping
    List<SensorReading> addSensorMultipleReadings(@RequestBody SensorMultipleReadings sensorReadings){
        return service.addSensorMultipleReadings(sensorReadings);
    }

}
