package irrigationsystem.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import irrigationsystem.cache.CacheService;
import irrigationsystem.entity.*;
import irrigationsystem.repository.MeasureTypeRepository;
import irrigationsystem.repository.SensorDataRepository;
import irrigationsystem.repository.SensorRepository;
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

    /**
     * Starts MQTT listener when the app is completely ready
     */
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
                    int controllerId = json.path("controllerId").asInt(-1);

                    if (controllerId == -1) {
                        log.warn("Missing controllerId in message: {}", json);
                        return;
                    }
                    double temperature = json.get("temperature").asDouble();
                    double humidity = json.get("humidity").asDouble();
                    double light = json.get("light").asDouble();

                    JsonNode soilMoistureNode = json.get("soilMoisture");

                    List<Double> soilMoistureValues = new ArrayList<>();

                    if (soilMoistureNode != null && soilMoistureNode.isArray()) {
                        for (JsonNode value : soilMoistureNode) {
                            soilMoistureValues.add(value.asDouble());
                        }
                    }

                    /*
                    Get sensors for a controller
                     */
                    List<Sensor> sensors = sensorRepository.findByControllerId(controllerId);
                    if (sensors == null || sensors.isEmpty()) {
                        log.warn("No sensors found for controller {}", controllerId);
                        return;
                    }

                    List<SensorData> sensorData = getSensorData(sensors, temperature, humidity, light, soilMoistureValues);

                    /*
                     Bulk save data in database
                     */
                    sensorDataRepository.saveAll(sensorData);

                } catch (Exception e) {
                    log.error("Error processing MQTT message", e);
                }
            });

            log.info("MQTT client started and subscribed to {}", topic);

        } catch (MqttException e) {
            log.error("Failed to start MQTT client", e);
        }
    }

    /**
     * Ensures that when application stops the MQTT client is disconnected
     */
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

    private List<SensorData> getSensorData(List<Sensor> sensors, double temperature, double humidity, double light, List<Double> soilMoisture) {
        List<SensorData> sensorData = new ArrayList<>();

        for (Sensor sensor : sensors) {
            List<MeasureType> types = measureTypeRepository.findBySensorTypes_Id(sensor.getSensorType().getId());

            for (MeasureType type : types) {
                try {
                    MeasureTypeEnum measureType = MeasureTypeEnum.valueOf(type.getName());

                    if (measureType == MeasureTypeEnum.Temperature) {
                        sensorData.add(createSensorData(sensor, temperature, type));
                    } else if (measureType == MeasureTypeEnum.Humidity) {
                        sensorData.add(createSensorData(sensor, humidity, type));
                    } else if (measureType == MeasureTypeEnum.Light) {
                        sensorData.add(createSensorData(sensor, light, type));
                    } else if (measureType == MeasureTypeEnum.SoilMoisture) {
                        for (int i = 0; i < soilMoisture.size(); i++) {
                            sensorData.add(createSensorData(sensor, soilMoisture.get(i), type));
                        }
                    }
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown measure type: {}", type.getName());
                }
            }
        }

        return sensorData;
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
