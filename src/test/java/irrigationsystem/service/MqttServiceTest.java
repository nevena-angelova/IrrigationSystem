package irrigationsystem.service;

import irrigationsystem.mqtt.MqttPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MqttServiceTest {
    @Mock MqttPublisher mqttPublisher;
    @InjectMocks MqttService mqttService;

    @Test
    void irrigate_shouldPublishExpectedTopicAndPayload() {
        mqttService.irrigate(7L, 3, 12.345);

        verify(mqttPublisher).publish("garden/7/relay/3", "{\"areaNumber\":3,\"irrigationDuration\":12.35}");
    }
}
