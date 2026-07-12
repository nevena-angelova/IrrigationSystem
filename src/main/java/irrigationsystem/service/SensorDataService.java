package irrigationsystem.service;

import irrigationsystem.entity.MeasureTypeEnum;
import irrigationsystem.model.*;
import irrigationsystem.repository.SensorDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public List<PlantSensorData> getLatestPlantSoilMoistureSensorData(Long userId) {
        List<PlantSensorData> plantSensorData = sensorDataRepository.getLatestValuesByUserId(userId);
        return plantSensorData;
    }

    public Map<Integer, ControllerMetrics> getControllerMetrics(LocalDateTime from) {

        List<ControllerSensorData> controllerSensorData = sensorDataRepository.getDataFrom(from, MeasureTypeEnum.SoilMoisture.getValue());

        if (controllerSensorData == null || controllerSensorData.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Integer, ControllerMetrics> result =
            controllerSensorData.stream()
                .collect(Collectors.groupingBy(
                    ControllerSensorData::getControllerId,
                    Collectors.collectingAndThen(Collectors.toList(), list -> {

                        DoubleSummaryStatistics tStats = list.stream()
                            .filter(x -> MeasureTypeEnum.Temperature.name().equals(x.getMeasureType()))
                            .mapToDouble(ControllerSensorData::getValue)
                            .summaryStatistics();

                        DoubleSummaryStatistics rhStats = list.stream()
                            .filter(x -> MeasureTypeEnum.Humidity.name().equals(x.getMeasureType()))
                            .mapToDouble(ControllerSensorData::getValue)
                            .summaryStatistics();

                        List<LightData> lightData = list.stream()
                            .filter(x -> MeasureTypeEnum.Light.name().equals(x.getMeasureType()))
                            .map(x -> new LightData(x.getValue(), x.getCreationDate()))
                            .toList();

                        ControllerSensorData first = list.get(0);

                        return new ControllerMetrics(
                            first.getControllerId(),
                            first.getAltitude(),
                            first.getLatitude(),
                            lightData,
                            new Metrics(
                                tStats.getCount() > 0 ? tStats.getMin() : 0,
                                tStats.getCount() > 0 ? tStats.getMax() : 0,
                                tStats.getCount() > 0 ? tStats.getAverage() : 0,
                                rhStats.getCount() > 0 ? rhStats.getMin() : 0,
                                rhStats.getCount() > 0 ? rhStats.getMax() : 0
                            ));
                    })
                ));

        return result;
    }

    public List<PlantSoilMoistureData> getLatestPlantSoilMoistureSensorData() {

        List<PlantSoilMoistureData> data = sensorDataRepository.getPlantSensorData(MeasureTypeEnum.SoilMoisture.getValue());

        return data == null ? Collections.emptyList() : data;
    }
}
