package com.example.sensor_readings.service;

import com.example.sensor_readings.model.Reading;
import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.model.SensorMultipleReadings;
import com.example.sensor_readings.repository.SensorReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensorMultipleReadingsService {

    @Autowired
    private SensorReadingRepository repository;

    /**
     * Add multiple readings of a sensor into storage
     * @param sensorMultipleReadings
     * @return List<SensorReading>
     */
    public List<SensorReading> addSensorMultipleReadings(SensorMultipleReadings sensorMultipleReadings){
        List<SensorReading> sensorReadingList = new ArrayList<>();
        String sensorId = sensorMultipleReadings.getSensorId();
        for (Reading reading : sensorMultipleReadings.getReadings()){
            sensorReadingList.add(new SensorReading(sensorId, reading));
        }
        return repository.saveAll(sensorReadingList);
    }

}
