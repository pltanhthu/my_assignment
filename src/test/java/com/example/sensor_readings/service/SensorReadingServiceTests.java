package com.example.sensor_readings.service;

import com.example.sensor_readings.exception.SensorReadingNotFoundException;
import com.example.sensor_readings.model.Reading;
import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.repository.SensorReadingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SensorReadingServiceTests {
    @Mock
    private SensorReadingRepository repository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SensorReadingService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSensorReading() {
        SensorReading reading = new SensorReading("sensor1", new Reading(LocalDateTime.parse("2025-05-30T18:00:00"), 20.5, 30.5));

        when(repository.save(any(SensorReading.class))).thenReturn(reading);

        SensorReading saved = service.addSensorReading(reading);

        assertThat(saved).isEqualTo(reading);
        verify(repository, times(1)).save(reading);
    }


    @ParameterizedTest
    @CsvSource(value = {"::false:", "sensor1:20.0,30.0:true:25.0", "sensor1::false:"}, delimiter = ':')
    void testGetAverageMetricValues(String sensorId, String expectedResultListStr, String expectedSuccessStr, String expectedResponseStr){

        List<Double> expectedResultList = new ArrayList<>();
        if (expectedResultListStr != null){
            expectedResultList = new ArrayList<Double>(Arrays.asList(expectedResultListStr.split(","))
                    .stream().map(Double::parseDouble).collect(Collectors.toList()));
        }

        boolean expectedSuccess = Boolean.valueOf(expectedSuccessStr);

        Double expectedResponse = null;
        if (expectedResponseStr != null){
            expectedResponse = Double.valueOf(expectedResponseStr);
        }


        String metric = "temperature";
        String from = "2025-05-29T00:00:00";
        String to = "2025-05-30T00:00:00";

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<Double> criteriaQuery = mock(CriteriaQuery.class);
        Root<SensorReading> root = mock(Root.class);
        TypedQuery typedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Double.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(SensorReading.class)).thenReturn(root);

        LocalDateTime dtFrom = LocalDateTime.parse(from);
        LocalDateTime dtTo = LocalDateTime.parse(to);

        Selection<Double> metricPath = mock(Selection.class);
        when(root.get("reading")).thenReturn(mock());

        when(criteriaQuery.select(metricPath)).thenReturn(criteriaQuery);

        Predicate sensorPredicate = null;
        if (sensorId != null) {
            sensorPredicate = mock(Predicate.class);
            when(criteriaBuilder.equal(root.get("sensorId"), sensorId)).thenReturn(sensorPredicate);
        }
        Predicate datePredicate = mock(Predicate.class);
        when(criteriaBuilder.between(root.get("timestamp"), dtFrom, dtTo)).thenReturn(datePredicate);
        when(criteriaQuery.where(sensorPredicate, datePredicate)).thenReturn(criteriaQuery);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedResultList);

        if (expectedSuccess){
            double result = service.getAverageMetricValues(metric, from, to, sensorId);
            assertThat(result).isEqualTo(expectedResponse);

            if (sensorId != null) {
                verify(criteriaQuery).where(any(Predicate.class), any(Predicate.class));
            } else {
                verify(criteriaQuery, never()).where((Predicate) any());
            }
        }
        else{
            assertThrows(SensorReadingNotFoundException.class, () -> {
                service.getAverageMetricValues(metric, from, to, null);
            });
        }

    }

}
