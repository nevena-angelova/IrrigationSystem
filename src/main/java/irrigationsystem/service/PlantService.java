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

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final Mapper mapper;
    private final SensorTypeRepository sensorTypeRepository;
    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;
    private final DeviceRepository deviceRepository;
    private final RelayRepository relayRepository;
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

        // Assign the first unused relay if available
        List<Relay> relays = device.getRelays();
        Optional<Relay> firstUnusedRelay = relays.stream()
                .filter(r -> !r.isUsed())
                .findFirst();

        firstUnusedRelay.ifPresent(relay -> {
            relay.setUsed(true);
            relayRepository.save(relay); // mark relay as used
            plant.setRelay(relay);       // assign relay BEFORE saving plant
        });

        // Save the plant AFTER relay is set
        plantRepository.save(plant);

        // Attach sensors
        var sensors = getSensors(plant, device);
        sensorRepository.saveAll(sensors);

        plant.getSensors().clear();
        plant.getSensors().addAll(sensors);

        // Save again only if you want the plant to have updated sensor references
        plantRepository.save(plant);

        return ResponseDto.<String>builder().value("Plant created successfully").build();
    }

    public ResponseDto<List<PlantDto>> getUserPlants() {
        var userId = getUserId();

        if (userId.isEmpty()) {
            return ResponseDto.<List<PlantDto>>builder().errorMessage(Optional.of("Invalid authentication.")).hasErrors(true).build();
        }

        List<MeasureValuesDto> measures = sensorDataRepository.getLatestValuesByUserId(userId.get());

        Map<Long, List<MeasureValuesDto>> measuresByPlant = new HashMap<>();

        /*
        groups measure data by Plant id
        each group will have Plant -> { measure type 1 data, measure type 2 data ...}
        for types like temperature, soilMoisture and pressure
        */
        for (var measure : measures) {
            if (!measuresByPlant.containsKey(measure.getPlantId())) {
                List<MeasureValuesDto> list = new ArrayList<>();
                list.add(measure);
                measuresByPlant.put(measure.getPlantId(), list);
            } else {
                measuresByPlant.get(measure.getPlantId()).add(measure);
            }
        }

        List<PlantDto> plants = new ArrayList<>();

        // get measure values for each group

        for (var group : measuresByPlant.entrySet()) {
            Map<MeasureTypeEnum, Double> measureValues = new HashMap<>();
            for (var measure : group.getValue()) {
                var measureType = MeasureTypeEnum.valueOf(measure.getMeasureType());
                measureValues.put(measureType, measure.getValue());
            }

            // each group values has same plant data so take first item
            MeasureValuesDto mv = group.getValue().getFirst();

            GrowthPhase growthPhase = cacheService.getGrowthPhase(mv.getPlantingDate(), mv.getPlantTypeId());
            PlantType plantType = cacheService.getPlantType(mv.getPlantTypeId());

            Analyzer analyzer = AnalyzerFactory.createAnalyzer(group.getKey(), plantType, growthPhase, measureValues);
            ReportDto report = analyzer.analyze();

            PlantDto plantDto = createPlantDto(mv);
            plantDto.setReport(report);

            plants.add(plantDto);
        }

        return ResponseDto.<List<PlantDto>>builder().value(plants).build();
    }

    public ResponseDto<String> irrigate(long deviceId, int relayId, int irrigationDuration) {
        String topic = "garden/" + deviceId + "/relay/" + relayId;
        mqttPublisher.publish(topic, String.format("{\"relayId\":%d,\"irrigationDuration\":\"%d\"}", relayId, irrigationDuration));

        return ResponseDto.<String>builder().value("Relay ON command sent to device " + deviceId).build();
    }


    private PlantDto createPlantDto(MeasureValuesDto measureValuesDto) {

        PlantDto PlantDto = new PlantDto();
        PlantDto.setId(measureValuesDto.getPlantId());
        PlantDto.setPlantType(measureValuesDto.getPlantTypeId(), measureValuesDto.getPlantTypeName());
        PlantDto.setPlantingDate(measureValuesDto.getPlantingDate());
        PlantDto.setDeviceId(measureValuesDto.getDeviceId());
        PlantDto.setRelayId(measureValuesDto.getRelayId());

        return PlantDto;
    }

    // Every Plant has a temperature, soilMoisture and a pressure sensors attached
    // Sensor has type and each type can measure one or two parameters
    private List<Sensor> getSensors(Plant Plant, Device device) {

        var sensorTypes = sensorTypeRepository.findByNameIn(List.of("DHT22", "BMP180"));
        List<Sensor> sensors = new ArrayList<>();

        for (var sensorType : sensorTypes) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(sensorType);
            sensor.setPlant(Plant);
            sensor.setDevice(device);
            sensors.add(sensor);
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
