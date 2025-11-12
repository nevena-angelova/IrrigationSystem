package irrigationsystem.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import irrigationsystem.cache.CacheService;
import irrigationsystem.controller.NotificationController;
import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.*;
import irrigationsystem.repository.MeasureTypeRepository;
import irrigationsystem.repository.SensorDataRepository;
import irrigationsystem.repository.SensorRepository;
import irrigationsystem.analyzer.Analyzer;
import irrigationsystem.analyzer.AnalyzerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttSubscriber {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final MeasureTypeRepository measureTypeRepository;
    private final CacheService cacheService;

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.topic:garden/+/sensors}")
    private String topic;

    private static final String CLIENT_ID = "JavaSpringClient";

    private final NotificationController notificationController;
    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;

    private MqttClient client;

    // Starts MQTT listener when the app is completely ready
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        start();
    }

    public void start() {
        try {
            client = new MqttClient(broker, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            client.connect(options);

            client.subscribe(topic, 1, (t, msg) -> {
                String payload = new String(msg.getPayload());
                log.info("Received MQTT message | Topic: {} | Payload: {}", t, payload);

                try {
                    JsonNode json = mapper.readTree(payload);
                    Long deviceId = json.get("deviceId").asLong();
                    double temperature = json.get("temperature").asDouble();
                    double humidity = json.get("humidity").asDouble();
                    double soilMoisture = json.get("soilMoisture").asDouble();

                    // get sensors for device
                    List<Sensor> sensors = sensorRepository.findByDeviceId(deviceId);
                    if (sensors == null || sensors.isEmpty()) {
                        log.warn("No sensors found for device {}", deviceId);
                        return;
                    }

                    // Organize sensor data by Plant
                    SensorProcessingResult result = processSensorMeasurements(sensors, temperature, humidity, soilMoisture);

                    // Analyze data and generate reports
                    List<ReportDto> reports = analyze(result.plantMeasures());

                    // Send web socket notifications
                    reports.forEach(notificationController::sendReport);

                    // Bulk save data in database
                    sensorDataRepository.saveAll(result.sensorData());

                } catch (Exception e) {
                    log.error("Error processing MQTT message", e);
                }
            });

            log.info("MQTT client started and subscribed to {}", topic);

        } catch (MqttException e) {
            log.error("Failed to start MQTT client", e);
        }
    }

    // Ensures that when application stops the MQTT client is disconnected
    @PreDestroy
    public void stop() {
        if (client != null && client.isConnected()) {
            try {
                client.disconnect();
                log.info("MQTT client disconnected");
            } catch (MqttException e) {
                log.error("Error while disconnecting MQTT client", e);
            }
        }
    }

    private List<ReportDto> analyze(Map<Plant, Map<MeasureTypeEnum, Double>> plantMeasures) {
        List<ReportDto> reports = new ArrayList<>();

        for (var entry : plantMeasures.entrySet()) {

            Plant plant = entry.getKey();
            GrowthPhase growthPhase = cacheService.getGrowthPhase(plant.getPlantingDate(), plant.getPlantType().getId());

            Analyzer analyzer = AnalyzerFactory.createAnalyzer(plant.getId(), plant.getPlantType(), growthPhase, entry.getValue());

            ReportDto report = analyzer.analyze();

            if (report != null) {
                reports.add(report);
            }

        }

        return reports;
    }

    private SensorProcessingResult processSensorMeasurements(List<Sensor> sensors, double temperature, double humidity, double soilMoisture) {

        List<SensorData> data = new ArrayList<>();
        Map<Plant, Map<MeasureTypeEnum, Double>> plantMeasures = new HashMap<>();

        for (var sensor : sensors) {
            Map<MeasureTypeEnum, Double> sensorValues = new EnumMap<>(MeasureTypeEnum.class);

            List<MeasureType> types = measureTypeRepository.findBySensorTypes_Id(sensor.getSensorType().getId());

            for (var type : types) {
                try {
                    var measureType = MeasureTypeEnum.valueOf(type.getName());
                    Double value = switch (measureType) {
                        case Temperature -> temperature;
                        case Humidity -> humidity;
                        case SoilMoisture -> soilMoisture;
                        default -> null;
                    };

                    if (value != null) {
                        data.add(createSensorData(sensor, value, type));
                        sensorValues.put(measureType, value);
                    }

                } catch (IllegalArgumentException e) {
                    log.warn("Unknown measure type: {}", type.getName());
                }
            }


            if (!sensorValues.isEmpty()) {
                plantMeasures
                        .computeIfAbsent(sensor.getPlant(), k -> new EnumMap<>(MeasureTypeEnum.class))
                        .putAll(sensorValues);
            }
        }

        return new SensorProcessingResult(plantMeasures, data);
    }

    private SensorData createSensorData(Sensor sensor, double value, MeasureType measureType) {
        SensorData data = new SensorData();
        data.setSensor(sensor);
        data.setValue(value);
        data.setTimestamp(OffsetDateTime.now());
        data.setMeasureType(measureType);
        return data;
    }
}