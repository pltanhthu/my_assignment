package com.example.sensor_readings.controller;

import com.example.sensor_readings.SensorReadingsApplication;
import com.example.sensor_readings.model.Reading;
import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.service.SensorReadingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SensorReadingsApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SensorReadingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SensorReadingService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddSensorReading() throws Exception {

        SensorReading reading = new SensorReading("sensor1", new Reading(LocalDateTime.parse("2025-05-30T18:00:00"), 20.5, 39.0));

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
    @CsvSource(value = {";temperature;2025-05-29T00:00:00;2025-05-31T21:00:00;20.0", ";temperature;2025-05-29T00:00:00;2025-05-30T21:00:00;15.0", "sensor1;humidity;2025-05-29T00:00:00;2025-05-31T00:00:00;35.0"}, delimiter = ';')
    public void testaverageMetricValuesInDateRange_success(String sensorId, String metric, String from, String to, String expectedResponseStr) throws Exception {
        double expectedResponse = Double.parseDouble(expectedResponseStr);

        mockMvc.perform(get("/sensor_readings/average")
                        .param("metric", metric)
                        .param("from", from)
                        .param("to", to)
                        .param("sensor_id", sensorId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expectedResponse));

    }

    @ParameterizedTest
    @ValueSource(strings = {"", "sensor3", "sensor2"})
    public void testaverageMetricValuesInDateRange_exception(String sensorId) throws Exception {
        String metric = "temperature";
        String from = "2025-05-28T00:00:00";
        String to = "2025-05-19T21:00:00";
        mockMvc.perform(get("/sensor_readings/average")
                        .param("metric", metric)
                        .param("from", from)
                        .param("to", to)
                        .param("sensor_id", sensorId)
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }
}
