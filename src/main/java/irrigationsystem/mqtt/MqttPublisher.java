package irrigationsystem.mqtt;

import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MqttPublisher {

    @Value("${mqtt.broker}")
    private String broker;

    private final String clientId = "SpringPublisher";

    public void publish(String topic, String payload) {
        try (MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence())) {
            mqttClient.connect();
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1); // Set Quality of Service level - 1: broker should send back confirmation of receipt, if not it will be resent
            mqttClient.publish(topic, message);
            mqttClient.disconnect();
            System.out.println("Published: " + payload + " topic: " + topic);
        } catch (MqttException e) {
            System.err.println("MQTT publish failed: " + e.getMessage());
        }
    }
}