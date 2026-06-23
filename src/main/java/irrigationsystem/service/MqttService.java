package irrigationsystem.service;

import irrigationsystem.dto.ResponseDto;
import irrigationsystem.mqtt.MqttPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MqttService {
    private final MqttPublisher mqttPublisher;

    public ResponseDto<String> irrigate(long controllerId, int areaNumber, double duration) {
        String topic = "garden/" + controllerId + "/relay/" + areaNumber;

        String payload = "ON:" + duration;

        mqttPublisher.publish(topic, payload);

        return ResponseDto.<String>builder().value("Relay ON command sent to controller " + controllerId).build();
    }
}
