# REST APIs FOR SENSOR READINGS 

This is an application which provides REST APIs for registering and retrieving sensor readings. It uses an in-memory database (H2) to store the data. The REST endpoints are defined on the default port 8080.
The Swagger API docs can be found at http://localhost:8080/swagger-ui/index.html# (after running the application)

## How to use
There are three endpoints you can call:

### Register a sensor reading
A sensor reading contains `sensorId` and `reading`. A `reading` is a measurement which has `timestamp`, `temperature`, and `humidity`.
```
{
    "sensorId": "sensor1",
    "reading":{
        "timestamp": "2025-05-30T23:01:01",
        "temperature": 20.0,
        "humidity": 50.55
    } 
}
```
The endpoint to register a sensor reading is:
```
POST /sensor_readings
Accept: application/json
Content-Type: application/json
{
    "sensorId": "sensor1",
    "reading":{
        "timestamp": "2025-05-30T23:01:01",
        "temperature": 20.0,
        "humidity": 50.55
    }
}
Response: HTTP 200
Content: sensor reading json similar as in request body 
```
For example:
```
curl --location 'localhost:8080/sensor_readings' \
--header 'Content-Type: application/json' \
--data '{
    "sensorId": "sensor1",
    "reading":{
        "timestamp": "2025-05-30T23:01:01",
        "temperature": 20.0,
        "humidity": 50.55
    }
}'
```

### Register multiple readings of a sensor
You can also register multiple readings of a sensor by using:
```
POST /sensor_multiple_readings
Accept: application/json
Content-Type: application/json
{
    "sensorId": "sensor1",
    "readings":[
        {
            "timestamp": "2025-05-28T23:02:01",
            "temperature": 22.0,
            "humidity": 52.0
        },
        {
            "timestamp": "2025-05-28T23:01:01",
            "temperature": 20.0,
            "humidity": 60.0
        }
    ]
}
Response: HTTP 200
Content: list of sensor readings 
```
For example:
```
curl --location 'localhost:8080/sensor_multiple_readings' \
--header 'Content-Type: application/json' \
--data '{
	"sensorId": "sensor2",
    "readings":[
        {
            "timestamp": "2025-05-28T23:02:01",
            "temperature": 42.0,
            "humidity": 52.0
        },
        {
            "timestamp": "2025-05-29T23:01:01",
            "temperature": 20.0,
            "humidity": 60.0
        }
    ]  
}'
```

### Get average of metric values
Get average of metric values  (either temperature or humidity) of a particular sensor or of all sensors in a specific date range.

```
GET /sensor_readings/average
Request params: sensor_id (optional), metric, from, to
Response: HTTP 200
Content: average value
```
For example:

1. Get average of temperature of a particular sensor in a date range
```
curl --location 'localhost:8080/sensor_readings/average?metric=temperature&from=2025-05-28T00%3A00%3A00&to=2025-05-28T20%3A00%3A00&sensor_id=sensor1'
```

2. Get average of humidity of all sensors in a date range
```
curl --location 'localhost:8080/sensor_readings/average?metric=temperature&from=2025-05-28T00%3A00%3A00&to=2025-05-28T20%3A00%3A00'
```

## How to run
The application is packaged as a jar in target folder:
- Clone the repository (The project uses JDK 21 and Maven 3.x)
- Run the application by the command:  
```
java -jar target/sensor-readings-0.0.1-SNAPSHOT.jar
```
**NOTE:**
- The application uses H2 in-memory storage with default settings. This means that the database is volatile, and results in data loss after application restart.
- To view and query the database you can browse to http://localhost:8080/h2-console. Default username is 'sa' with a blank password and JDBC URL is `jdbc:h2:mem:testdb`. 





