package irrigationsystem.service;

import irrigationsystem.entity.MeasureType;
import irrigationsystem.entity.MeasureTypeEnum;
import irrigationsystem.entity.Sensor;
import irrigationsystem.entity.SensorData;
import irrigationsystem.model.*;
import irrigationsystem.repository.MeasureTypeRepository;
import irrigationsystem.repository.SensorDataRepository;
import irrigationsystem.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataService {
    private final MeasureTypeRepository measureTypeRepository;
    private final SensorDataRepository sensorDataRepository;
    private final SensorRepository sensorRepository;

    public List<PlantSensorData> getLatestPlantSensorData(Long userId) {
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

    public List<PlantSoilMoistureData> getLatestPlantSensorData() {

        List<PlantSoilMoistureData> data = sensorDataRepository.getPlantSoilMoistureSensorData(MeasureTypeEnum.SoilMoisture.getValue());

        return data == null ? Collections.emptyList() : data;
    }

    public void saveSensorData(int controllerId, double temperature, double humidity, double light, List<Double> soilMoisture) {
         /*
         Get sensors for a controller
          */
        List<Sensor> sensors = sensorRepository.findByControllerId(controllerId);
        if (sensors == null || sensors.isEmpty()) {
            log.warn("No sensors found for controller {}", controllerId);
            return;
        }

        List<SensorData> sensorData = new ArrayList<>();
        int soilMoistureIndex = 0;
        for (Sensor sensor : sensors) {
            List<MeasureType> types = measureTypeRepository.findBySensorTypes_Id(sensor.getSensorType().getId());

            for (MeasureType type : types) {
                try {
                    MeasureTypeEnum measureType = MeasureTypeEnum.valueOf(type.getName());

                    if (measureType == MeasureTypeEnum.Temperature) {
                        sensorData.add(createSensorData(sensor, temperature, type));
                    } else if (measureType == MeasureTypeEnum.Humidity) {
                        sensorData.add(createSensorData(sensor, humidity, type));
                    } else if (measureType == MeasureTypeEnum.Light) {
                        sensorData.add(createSensorData(sensor, light, type));
                    } else if (measureType == MeasureTypeEnum.SoilMoisture) {
                        if (soilMoistureIndex < soilMoisture.size()) {
                            sensorData.add(createSensorData(sensor, soilMoisture.get(soilMoistureIndex), type));
                            soilMoistureIndex++;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown measure type: {}", type.getName());
                }
            }
        }
        /*
        Bulk save data in database
        */
        sensorDataRepository.saveAll(sensorData);
    }

    private SensorData createSensorData(Sensor sensor, double value, MeasureType measureType) {
        SensorData data = new SensorData();
        data.setSensor(sensor);
        data.setValue(value);
        data.setTimestamp(OffsetDateTime.now());
        data.setMeasureType(measureType);
        return data;
    }
}
