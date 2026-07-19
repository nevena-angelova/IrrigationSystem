package irrigationsystem.service;

import irrigationsystem.analyzer.Analyzer;
import irrigationsystem.analyzer.AnalyzerFactory;
import irrigationsystem.cache.CacheService;
import irrigationsystem.dto.*;
import irrigationsystem.mapper.Mapper;
import irrigationsystem.entity.*;
import irrigationsystem.model.PlantSensorData;
import irrigationsystem.model.SensorValues;
import irrigationsystem.repository.*;
import irrigationsystem.mqtt.MqttPublisher;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final Mapper mapper;
    private final SensorTypeRepository sensorTypeRepository;
    private final ControllerRepository controllerRepository;
    private final MqttPublisher mqttPublisher;
    private final CacheService cacheService;
    private final SensorDataService sensorDataService;
    private final SensorRepository sensorRepository;

    public ResponseDto<List<PlantTypeDto>> getPlantTypes() {

        var plantTypes = mapper.toPlantTypeDtoList(cacheService.plantTypes());

        return ResponseDto.<List<PlantTypeDto>>builder().value(plantTypes).build();
    }

    @Transactional
    public ResponseDto<String> createPlant(CreatePlantDto createPlantDto) {

        var userId = getUserId();

        if (userId.isEmpty()) {
            return ResponseDto.<String>builder().errorMessage(Optional.of("Invalid authentication.")).hasErrors(true).build();
        }

        Controller controller = controllerRepository.getControllerByUserId(userId.get());

        Plant plant = createNewPlant(createPlantDto);

        setAreaNumber(plant, controller.getId());

        Plant savedPlant = plantRepository.save(plant);

        Sensor soilMoistureSensor = getSoilMoistureSensor();

        soilMoistureSensor.setController(controller);
        soilMoistureSensor.setPlant(savedPlant);

        savedPlant.addSensor(soilMoistureSensor);

        sensorRepository.save(soilMoistureSensor);

        return ResponseDto.<String>builder().value("Plant created successfully").build();
    }

    private void setAreaNumber(Plant plant, Integer controllerId) {
        int plantCount = plantRepository.getPlantCount(controllerId);
        plant.setAreaNumber(plantCount + 1);
    }

    public ResponseDto<List<PlantReportDto>> getUserPlantReports() {
        Optional<Long> userId = getUserId();

        if (userId.isEmpty()) {
            return ResponseDto.<List<PlantReportDto>>builder().errorMessage(Optional.of("Invalid authentication.")).hasErrors(true).build();
        }

        List<PlantReportDto> plantReport = getPlantReport(userId.get());

        return ResponseDto.<List<PlantReportDto>>builder().value(plantReport).build();
    }

    private Plant createNewPlant(CreatePlantDto createPlantDto) {
        Plant plant = new Plant();
        plant.setPlantTypeId(createPlantDto.getPlantTypeId());
        plant.setPlantingDate(createPlantDto.getPlantingDate());
        plant.setDistanceX(createPlantDto.getDistanceX());
        plant.setDistanceY(createPlantDto.getDistanceY());
        plant.setEtc(0.0);
        plant.setEmitterFlow(createPlantDto.getEmitterFlow());
        return plant;
    }

    private Sensor getSoilMoistureSensor() {

        SensorType soilMoistureType = sensorTypeRepository.findByNameIn(List.of(SensorTypeEnum.SOIL_MOISTURE.getValue())).getFirst();

        Sensor soilMoistureSensor = new Sensor();
        soilMoistureSensor.setSensorType(soilMoistureType);

        return soilMoistureSensor;
    }

    private Optional<Long> getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return Optional.empty();
        }

        long userId = ((User) authentication.getPrincipal()).getId();

        return Optional.of(userId);
    }

    private List<PlantReportDto> getPlantReport(Long userId) {
        List<PlantSensorData> plantSensorData = sensorDataService.getPlantSoilMoistureSensorData(userId);

        /*
        Group measures by PlantId, then for each group create a PlantReportDto with the latest measure values and an analysis report.
        */
        return plantSensorData.stream()
            .collect(Collectors.groupingBy(PlantSensorData::getPlantId))
            .values()
            .stream()
            .map(this::analyzePlantData)
            .toList();
    }

    private PlantReportDto analyzePlantData(List<PlantSensorData> sensorData) {

        PlantSensorData data = sensorData.getFirst();

        SensorValues sensorValues = new SensorValues();

        GrowthPhase growthPhase = cacheService.getGrowthPhase(data.getPlantingDate(), data.getPlantTypeId());

        sensorData.forEach(v -> {
            MeasureTypeEnum type = MeasureTypeEnum.valueOf(v.getMeasureType());

            switch (type) {
                case Temperature -> sensorValues.setTemperature(v.getValue());
                case SoilMoisture -> sensorValues.setSoilMoisture(v.getValue());
                case Humidity -> sensorValues.setHumidity(v.getValue());
                case Light -> sensorValues.setLight(v.getValue());
            }
        });

        Analyzer analyzer = AnalyzerFactory.createAnalyzer(
            data.getPlantId(),
            sensorValues,
            data.getPlantTypeId(),
            growthPhase
        );

        ReportDto report = analyzer.analyze();

        return new PlantReportDto(data, report);
    }
}
