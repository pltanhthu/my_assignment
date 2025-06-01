package com.example.sensor_readings.controller;

import com.example.sensor_readings.exception.SensorReadingNotFoundException;
import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.service.SensorReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/sensor_readings")
public class SensorReadingController {

    @Autowired
    private SensorReadingService service;

    @Tag(name = "post", description = "Register sensor readings APIs")
    @Operation(summary = "Register a sensor reading",
            description = "Register a sensor reading")
    @PostMapping
    SensorReading addSensorReading(@RequestBody SensorReading sensorReading){
        return service.addSensorReading(sensorReading);
    }

    @Tag(name = "get", description = "Get average of metric values")
    @Operation(summary = "Get average of metric values",
            description = "Get average of metric values of a sensor or of all sensors in specific date range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Double.class))}),
            @ApiResponse(responseCode = "404", description = "Sensor reading not found",
                    content = @Content)})
    @GetMapping("/average")
    double averageMetricValuesInDateRange(@Parameter(
            description = "Id of the sensor whose metric values will be calculated. To get average of metric values of all sensors, do not set this parameter.",
            required = false) @RequestParam(required = false, name="sensor_id") String sensorId,
              @Parameter(
                      description = "The name of metric to be calculated",
                      required = true)
              @RequestParam String metric,
              @Parameter(
                      description = "The start datetime in ISO format",
                      required = true)
              @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) String from,
              @Parameter(
                      description = "The end datetime in ISO format",
                      required = true)
              @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) String to){

        try{
            if (sensorId == null){
                service.getAverageMetricValues(metric, from, to, null);
            }
            return service.getAverageMetricValues(metric, from, to, sensorId);
        }
        catch(SensorReadingNotFoundException ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }

    }
}
