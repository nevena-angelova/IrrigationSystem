package irrigationsystem.service;

import irrigationsystem.cache.CacheService;
import irrigationsystem.entity.GrowthPhase;
import irrigationsystem.entity.Plant;
import irrigationsystem.entity.PlantType;
import irrigationsystem.model.ControllerMetrics;
import irrigationsystem.model.Metrics;
import irrigationsystem.model.PlantSoilMoistureData;
import irrigationsystem.repository.ControllerRepository;
import irrigationsystem.repository.EtcStatisticRepository;
import irrigationsystem.repository.PlantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IrrigationServiceTest {
    @Mock SensorDataService sensorDataService;
    @Mock MqttService mqttService;
    @Mock PlantRepository plantRepository;
    @Mock EtcStatisticRepository etcStatisticRepository;
    @Mock ControllerRepository controllerRepository;
    @Mock CacheService cacheService;
    @InjectMocks IrrigationService service;

    @Test
    void processDailyIrrigation_shouldReturnWhenControllerMetricsAreEmpty() {
        when(sensorDataService.getControllerMetrics(any())).thenReturn(Map.of());

        service.processDailyIrrigation();

        verify(sensorDataService, never()).getPlantSoilMoistureSensorData();
        verifyNoInteractions(plantRepository, mqttService, etcStatisticRepository);
    }

    @Test
    void processDailyIrrigation_shouldIrrigatePlantWhenSoilIsBelowMinimum() {
        Plant plant = new Plant();
        plant.setId(1L);
        plant.setPlantingDate(java.time.LocalDate.now().minusDays(10));
        plant.setPlantTypeId(1);
        plant.setDistanceX(50);
        plant.setDistanceY(40);
        plant.setEmitterFlow(2);
        plant.setAreaNumber(3);
        plant.setEtc(0);

        GrowthPhase phase = new GrowthPhase(0, 100, "Test", "", 40, 70, plantType(), 1.0);
        ControllerMetrics metrics = new ControllerMetrics(7, 100, 42.7, List.of(), new Metrics(10, 25, 17, 40, 80));
        PlantSoilMoistureData data = mock(PlantSoilMoistureData.class);
        when(data.getPlantId()).thenReturn(1L);
        when(data.getControllerId()).thenReturn(7);
        when(data.getSoilMoisture()).thenReturn(20.0);

        when(sensorDataService.getControllerMetrics(any())).thenReturn(Map.of(7, metrics));
        when(sensorDataService.getPlantSoilMoistureSensorData()).thenReturn(List.of(data));
        when(plantRepository.findAll()).thenReturn(List.of(plant));
        when(cacheService.getGrowthPhase(any(), eq(1))).thenReturn(phase);

        service.processDailyIrrigation();

        verify(mqttService).irrigate(eq(7L), eq(3), anyDouble());
        verify(etcStatisticRepository).save(any());
        verify(plantRepository).saveAll(anyList());
        assertEquals(0.0, plant.getEtc());
    }

    private PlantType plantType() {
        PlantType type = new PlantType();
        type.setId(1);
        return type;
    }
}
