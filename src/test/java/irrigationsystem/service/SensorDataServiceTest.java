package irrigationsystem.service;

import irrigationsystem.entity.MeasureType;
import irrigationsystem.entity.Sensor;
import irrigationsystem.entity.SensorData;
import irrigationsystem.entity.SensorType;
import irrigationsystem.model.ControllerMetrics;
import irrigationsystem.model.ControllerSensorData;
import irrigationsystem.model.Metrics;
import irrigationsystem.repository.MeasureTypeRepository;
import irrigationsystem.repository.SensorDataRepository;
import irrigationsystem.repository.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorDataServiceTest {
    @Mock
    MeasureTypeRepository measureTypeRepository;
    @Mock
    SensorDataRepository sensorDataRepository;
    @Mock
    SensorRepository sensorRepository;
    @InjectMocks
    SensorDataService service;

    @Test
    void getControllerMetrics_shouldReturnEmptyMapForNoData() {
        when(sensorDataRepository.getCommonSensorDataFrom(any())).thenReturn(List.of());
        assertTrue(service.getControllerMetrics(LocalDateTime.now()).isEmpty());
    }

    @Test
    void getControllerMetrics_shouldCalculateTemperatureHumidityAndLightMetrics() {
        LocalDateTime now = LocalDateTime.now();

        ControllerSensorData t1 = firstRow(5, 100, 42, "Temperature", 10.0);
        ControllerSensorData t2 = row(5, "Temperature", 20.0);
        ControllerSensorData h1 = row(5, "Humidity", 40.0);
        ControllerSensorData h2 = row(5, "Humidity", 80.0);
        ControllerSensorData l = lightRow(5, 1000.0, now);

        when(sensorDataRepository.getCommonSensorDataFrom(any())).thenReturn(List.of(t1, t2, h1, h2, l));

        Map<Integer, ControllerMetrics> result = service.getControllerMetrics(now.minusDays(1));
        ControllerMetrics metrics = result.get(5);

        assertNotNull(metrics);
        assertEquals(100, metrics.getAltitude());
        assertEquals(42, metrics.getLatitude());
        Metrics m = metrics.getMetrics();
        assertEquals(10, m.getTMin());
        assertEquals(20, m.getTMax());
        assertEquals(15, m.getTMean());
        assertEquals(40, m.getRhMin());
        assertEquals(80, m.getRhMax());
        assertEquals(1, metrics.getLightData().size());
    }

    @Test
    void saveSensorData_shouldCreateAndSaveMappedSensorData() {
        SensorType sensorType = new SensorType();
        sensorType.setId(1);
        Sensor sensor = new Sensor();
        sensor.setSensorType(sensorType);
        MeasureType temperature = measure("Temperature");
        MeasureType humidity = measure("Humidity");
        MeasureType light = measure("Light");
        MeasureType soil = measure("SoilMoisture");
        when(sensorRepository.findByControllerIdOrderByIdAsc(7)).thenReturn(List.of(sensor));
        when(measureTypeRepository.findBySensorTypes_Id(1)).thenReturn(List.of(temperature, humidity, light, soil));

        service.saveSensorData(7, 21, 60, 500, List.of(35.0));

        ArgumentCaptor<Iterable<SensorData>> captor = ArgumentCaptor.forClass(Iterable.class);
        verify(sensorDataRepository).saveAll(captor.capture());
        assertEquals(4, ((Collection<SensorData>) captor.getValue()).size());
    }

    private MeasureType measure(String name) {
        MeasureType m = new MeasureType();
        m.setName(name);
        return m;
    }

    private ControllerSensorData firstRow(int id, double altitude, double latitude, String type, double value) {
        ControllerSensorData row = mock(ControllerSensorData.class);
        when(row.getControllerId()).thenReturn(id);
        when(row.getAltitude()).thenReturn(altitude);
        when(row.getLatitude()).thenReturn(latitude);
        when(row.getMeasureType()).thenReturn(type);
        when(row.getValue()).thenReturn(value);
        return row;
    }

    private ControllerSensorData row(int id, String type, double value) {
        ControllerSensorData row = mock(ControllerSensorData.class);
        when(row.getControllerId()).thenReturn(id);
        when(row.getMeasureType()).thenReturn(type);
        when(row.getValue()).thenReturn(value);
        return row;
    }

    private ControllerSensorData lightRow(int id, double value, LocalDateTime date) {
        ControllerSensorData row = mock(ControllerSensorData.class);
        when(row.getControllerId()).thenReturn(id);
        when(row.getMeasureType()).thenReturn("Light");
        when(row.getValue()).thenReturn(value);
        when(row.getCreationDate()).thenReturn(date);
        return row;
    }
}
