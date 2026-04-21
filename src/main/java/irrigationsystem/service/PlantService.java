package irrigationsystem.service;

import irrigationsystem.cache.CacheService;
import irrigationsystem.dto.*;
import irrigationsystem.mapper.Mapper;
import irrigationsystem.model.*;
import irrigationsystem.repository.*;
import irrigationsystem.analyzer.Analyzer;
import irrigationsystem.analyzer.AnalyzerFactory;
import irrigationsystem.mqtt.MqttPublisher;

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
    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;
    private final DeviceRepository deviceRepository;
    private final MqttPublisher mqttPublisher;
    private final CacheService cacheService;

    public ResponseDto<List<PlantTypeDto>> getPlantTypes() {

        var plantTypes = mapper.toPlantTypeDtoList(cacheService.plantTypes());

        return ResponseDto.<List<PlantTypeDto>>builder().value(plantTypes).build();
    }

    public ResponseDto<String> createPlant(CreatePlantDto createPlantDto) {

        var userId = getUserId();

        if (userId.isEmpty()) {
            return ResponseDto.<String>builder().errorMessage(Optional.of("Invalid authentication.")).hasErrors(true).build();
        }

        Device device = deviceRepository.getDeviceByUserId(userId.get());

        Plant plant = new Plant();
        plant.setPlantTypeId(createPlantDto.getPlantTypeId());
        plant.setPlantingDate(createPlantDto.getPlantingDate());
        plant.setRelayNumber(1);

        /*
         Save the plant after relay is set
         */
        plantRepository.save(plant);

        /*
          Attach sensors
         */
        attachSensors(plant, device);

       /*
        Save again only if the plant needs to have updated sensor references
        */
        plantRepository.save(plant);

        return ResponseDto.<String>builder().value("Plant created successfully").build();
    }

    public ResponseDto<List<PlantDto>> getUserPlants() {
        Optional<Long> userId = getUserId();

        if (userId.isEmpty()) {
            return ResponseDto.<List<PlantDto>>builder().errorMessage(Optional.of("Invalid authentication.")).hasErrors(true).build();
        }

        List<MeasureValuesDto> measures = sensorDataRepository.getLatestValuesByUserId(userId.get());

        /*
          Group measures by Plant ID, then for each group create a PlantDto with the latest measure values and an analysis report.
        */
        List<PlantDto> plants =
                measures.stream()
                        .collect(Collectors.groupingBy(MeasureValuesDto::getPlantId))
                        .values()
                        .stream()
                        .map(this::buildPlantDto)
                        .toList();

        return ResponseDto.<List<PlantDto>>builder().value(plants).build();
    }

    public List<PlantDto> getAllPlants() {

        List<MeasureValuesDto> measures = sensorDataRepository.getLatestValuesAllUsers();

       /*
          Group measures by Plant ID, then for each group create a PlantDto with the latest measure values and an analysis report.
        */
        List<PlantDto> plants =
                measures.stream()
                        .collect(Collectors.groupingBy(MeasureValuesDto::getPlantId))
                        .values()
                        .stream()
                        .map(this::buildPlantDto)
                        .toList();

        return plants;
    }

    public ResponseDto<String> irrigate(long deviceId, int relayId, int irrigationDuration) {
        String topic = "garden/" + deviceId + "/relay/" + relayId;
        mqttPublisher.publish(topic, String.format("{\"relayId\":%d,\"irrigationDuration\":\"%d\"}", relayId, irrigationDuration));

        return ResponseDto.<String>builder().value("Relay ON command sent to device " + deviceId).build();
    }

    private PlantDto buildPlantDto(List<MeasureValuesDto> group) {
        MeasureValuesDto mv = group.getFirst();

        Map<MeasureTypeEnum, Double> measureValues =
                group.stream().collect(Collectors.toMap(
                        m -> MeasureTypeEnum.valueOf(m.getMeasureType()),
                        MeasureValuesDto::getValue
                ));

        GrowthPhase growthPhase = cacheService.getGrowthPhase(
                mv.getPlantingDate(), mv.getPlantTypeId()
        );

        PlantType plantType = cacheService.getPlantType(mv.getPlantTypeId());

        Analyzer analyzer = AnalyzerFactory.createAnalyzer(
                mv.getPlantId(),
                plantType,
                growthPhase,
                measureValues,
                true
        );

        ReportDto report = analyzer.analyze();

        PlantDto plantDto = createPlantDto(mv);
        plantDto.setReport(report);

        return plantDto;
    }

    private PlantDto createPlantDto(MeasureValuesDto measureValuesDto) {

        PlantDto plantDto = new PlantDto();
        plantDto.setId(measureValuesDto.getPlantId());
        plantDto.setPlantType(measureValuesDto.getPlantTypeId(), measureValuesDto.getPlantTypeName());
        plantDto.setPlantingDate(measureValuesDto.getPlantingDate());
        plantDto.setDeviceId(measureValuesDto.getDeviceId());
        plantDto.setRelayNumber(measureValuesDto.getRelayNumber());

        return plantDto;
    }

    /*
        Every plant has a temperature, soilMoisture and a pressure sensors attached
        Sensor has type and each type can measure one or two parameters
    */

    private List<Sensor> attachSensors(Plant plant, Device device) {

        var sensorTypes = sensorTypeRepository.findAll();
        List<Sensor> sensors = new ArrayList<>();

        for (SensorType type : sensorTypes) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(type);
            sensor.setDevice(device);
            sensor.setPlant(plant);
            plant.getSensors().add(sensor);
        }

        return sensors;
    }

    private Optional<Long> getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return Optional.empty();
        }

        long userId = ((User) authentication.getPrincipal()).getId();

        return Optional.of(userId);
    }
}
