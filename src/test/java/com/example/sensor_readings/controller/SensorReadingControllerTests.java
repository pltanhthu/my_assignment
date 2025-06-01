package com.example.sensor_readings.controller;

import com.example.sensor_readings.exception.SensorReadingNotFoundException;
import com.example.sensor_readings.model.Reading;
import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.service.SensorReadingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(SensorReadingController.class)
public class SensorReadingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorReadingService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddSensorReading() throws Exception {
        SensorReading reading = new SensorReading("sensor1", new Reading(LocalDateTime.parse("2025-05-30T18:00:00"), 20.5, 39.0));
        when(service.addSensorReading(Mockito.any(SensorReading.class))).thenReturn(reading);

        mockMvc.perform(post("/sensor_readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reading)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.sensorId").value("sensor1"))
                        .andExpect(jsonPath("$.reading.timestamp").value("2025-05-30T18:00:00"))
                        .andExpect(jsonPath("$.reading.temperature").value(20.5))
                        .andExpect(jsonPath("$.reading.humidity").value(39.0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"20.0", ""})
    public void testaverageMetricValuesInDateRange(String expectedServiceResponseStr) throws Exception {

        String metric = "temperature";
        String from = "2025-05-29T00:00:00";
        String to = "2025-05-30T00:00:00";

        Double expectedServiceResponse = null;
        if (!expectedServiceResponseStr.isEmpty()){
            expectedServiceResponse = Double.parseDouble(expectedServiceResponseStr);
            when(service.getAverageMetricValues(metric, from, to, null)).thenReturn(expectedServiceResponse.doubleValue());
            mockMvc.perform(get("/sensor_readings/average")
                            .param("metric", metric)
                            .param("from", from)
                            .param("to", to)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(expectedServiceResponse.doubleValue()));
        }
        else{
            when(service.getAverageMetricValues(metric, from, to, null)).thenThrow(SensorReadingNotFoundException.class);
            mockMvc.perform(get("/sensor_readings/average")
                            .param("metric", metric)
                            .param("from", from)
                            .param("to", to)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));

        }


    }
}
