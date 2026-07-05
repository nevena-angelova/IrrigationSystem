package irrigationsystem.service;

import irrigationsystem.mqtt.MqttPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MqttService {
    private final MqttPublisher mqttPublisher;

    public void irrigate(long controllerId, int areaNumber, double duration) {

        String topic = "garden/" + controllerId + "/relay/" + areaNumber;

        String payload = String.format(
            Locale.US,
            "{\"areaNumber\":%d,\"irrigationDuration\":%.2f}",
            areaNumber,
            duration
        );

        mqttPublisher.publish(topic, payload);
    }
}
