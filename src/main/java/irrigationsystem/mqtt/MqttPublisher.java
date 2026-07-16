package irrigationsystem.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqttPublisher {

    @Value("${mqtt.broker}")
    private String broker;

    public void publish(String topic, String payload) {
        String clientId = "SpringPublisher";
        try (MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence())) {
            client.connect();
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1); /* Set Quality of Service level - 1: broker should send back confirmation of receipt, if not, it will be resent */
            client.publish(topic, message);
            client.disconnect();
            log.info("Published: {} topic: {}", payload, topic);
        } catch (MqttException e) {
            log.error("MQTT publish failed: {}", e.getMessage());
        }
    }
}
