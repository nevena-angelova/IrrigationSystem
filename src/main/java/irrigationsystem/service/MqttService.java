package irrigationsystem.service;

import irrigationsystem.dto.ResponseDto;
import irrigationsystem.mqtt.MqttPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttService {
    private final MqttPublisher mqttPublisher;

    public ResponseDto<String> turnRelayOn(long controllerId, int areaNumber, double duration) {
        String topic = "garden/" + controllerId + "/relay/" + areaNumber;

        String payload = "ON:" + duration;

        mqttPublisher.publish(topic, payload);

        return ResponseDto.<String>builder().value("Relay ON command sent to controller " + controllerId).build();
    }

    public ResponseDto<String>  turnRelayOff(long controllerId) {
        String topic = "garden/" + controllerId + "/relay";
        mqttPublisher.publish(topic, "OFF");

        return ResponseDto.<String>builder().value("Relay OFF command sent to controller " + controllerId).build();
    }
}
