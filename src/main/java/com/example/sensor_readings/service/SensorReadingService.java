package com.example.sensor_readings.service;

import com.example.sensor_readings.exception.SensorReadingNotFoundException;
import com.example.sensor_readings.model.SensorReading;
import com.example.sensor_readings.repository.SensorReadingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorReadingService {

    @Autowired
    private SensorReadingRepository repository;

    @Autowired
    private EntityManager entityManager;

    /**
     * Store new sensorReading into repository
     * @param sensorReading
     * @return SensorReading object
     */
    public SensorReading addSensorReading(SensorReading sensorReading){
        return repository.save(sensorReading);
    }

    /**
     * Calculate the average of a particular metric of a particular sensor or of all sensor in the specific date range
     * @param metric
     * @param from
     * @param to
     * @param sensorId
     * @return double
     * @raise SensorReadingNotFoundException
     */
    public double getAverageMetricValues(final String metric, final String from, final String to, final String sensorId) {
        LocalDateTime dtFrom = LocalDateTime.parse(from);
        LocalDateTime dtTo = LocalDateTime.parse(to);

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
        final Root<SensorReading> root = criteriaQuery.from(SensorReading.class);

        Selection<Double> path;
        Predicate sensorIdPredicate = null;
        if (sensorId != null) {
            sensorIdPredicate = criteriaBuilder.equal(root.get("sensorId"), sensorId);
        }
        Predicate dateRangePredicate = criteriaBuilder.between(root.get("reading").get("timestamp"), dtFrom, dtTo);
        criteriaQuery.where(sensorIdPredicate, dateRangePredicate);

        path = root.get("reading").get(metric);
        criteriaQuery.select(path);

        List<Double> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        entityManager.close();
        double average = resultList
                .stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(Double.NaN);

        if (Double.isNaN(average)){
            if (sensorId == null){
                throw new SensorReadingNotFoundException();
            }
            else{
                throw new SensorReadingNotFoundException(sensorId);
            }
        }
        return average;
    }

}
