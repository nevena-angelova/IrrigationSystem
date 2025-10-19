package irrigationsystem.service;

import irrigationsystem.dto.ResponseDto;
import irrigationsystem.mqtt.MqttPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelayService {
    private final MqttPublisher mqttPublisher;

    public ResponseDto<String> turnRelayOn(long deviceId) {
        String topic = "garden/" + deviceId + "/relay";
        mqttPublisher.publish(topic, "ON");

        return ResponseDto.<String>builder().value("Relay ON command sent to device " + deviceId).build();
    }

    public ResponseDto<String>  turnRelayOff(long deviceId) {
        String topic = "garden/" + deviceId + "/relay";
        mqttPublisher.publish(topic, "OFF");

        return ResponseDto.<String>builder().value("Relay OFF command sent to device " + deviceId).build();
    }
}
