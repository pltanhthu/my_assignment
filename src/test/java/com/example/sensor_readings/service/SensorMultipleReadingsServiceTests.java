package com.example.sensor_readings.service;

import com.example.sensor_readings.model.Reading;
import com.example.sensor_readings.model.SensorMultipleReadings;
import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.repository.SensorReadingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SensorMultipleReadingsServiceTests {
    @Mock
    private SensorReadingRepository repository;

    @InjectMocks
    private SensorMultipleReadingsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSensorMultipleReadings() {
        List<Reading> readings = new ArrayList<>();
        readings.add(new Reading(LocalDateTime.parse("2025-05-30T18:00:00"), 20.5, 30.5));
        readings.add(new Reading(LocalDateTime.parse("2025-05-30T18:05:00"), 20.6, 30.7));
        SensorMultipleReadings sensorMultipleReadings = new SensorMultipleReadings("sensor1", readings);

        List<SensorReading> sensorReadings = new ArrayList<SensorReading>();
        for(Reading reading : readings){
            sensorReadings.add(new SensorReading("sensor1", reading));
        }

        when(repository.saveAll(any())).thenReturn(sensorReadings);

        List<SensorReading> saved = service.addSensorMultipleReadings(sensorMultipleReadings);
        assertThat(saved).isEqualTo(sensorReadings);
    }

}
