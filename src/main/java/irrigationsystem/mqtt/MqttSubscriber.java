package irrigationsystem.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import irrigationsystem.entity.*;
import irrigationsystem.service.SensorDataService;
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


import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttSubscriber {

    private final SensorDataService sensorDataService;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.topic:garden/+/sensors}")
    private String topic;

    private static final String CLIENT_ID = "JavaSpringClient";


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

                    sensorDataService.saveSensorData(controllerId, temperature, humidity, light, soilMoistureValues);

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

}
