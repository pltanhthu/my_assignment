package com.example.sensor_readings.controller;

import com.example.sensor_readings.model.Reading;
import com.example.sensor_readings.model.SensorMultipleReadings;
import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.service.SensorMultipleReadingsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SensorMultipleReadingsController.class)
public class SensorMultipleReadingsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorMultipleReadingsService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddSensorMultipleReadings() throws Exception {

        List<Reading> readings = new ArrayList<>();
        readings.add(new Reading(LocalDateTime.parse("2025-05-30T18:00:00"), 20.5, 30.5));
        readings.add(new Reading(LocalDateTime.parse("2025-05-30T18:05:00"), 20.6, 30.7));
        SensorMultipleReadings sensorMultipleReadings = new SensorMultipleReadings("sensor1", readings);

        List<SensorReading> sensorReadings = new ArrayList<SensorReading>();
        for(Reading reading : readings){
            sensorReadings.add(new SensorReading("sensor1", reading));
        }

        when(service.addSensorMultipleReadings(Mockito.any())).thenReturn(sensorReadings);

        mockMvc.perform(post("/sensor_multiple_readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorMultipleReadings)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].sensorId").value("sensor1"))
                        .andExpect(jsonPath("$.[0].reading.timestamp").value("2025-05-30T18:00:00"))
                        .andExpect(jsonPath("$.[0].reading.temperature").value(20.5))
                        .andExpect(jsonPath("$.[0].reading.humidity").value(30.5))
                        .andExpect(jsonPath("$.[1].sensorId").value("sensor1"))
                        .andExpect(jsonPath("$.[1].reading.timestamp").value("2025-05-30T18:05:00"))
                        .andExpect(jsonPath("$.[1].reading.temperature").value(20.6))
                        .andExpect(jsonPath("$.[1].reading.humidity").value(30.7));
    }

}
