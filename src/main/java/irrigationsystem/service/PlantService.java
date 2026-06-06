package irrigationsystem.service;

import irrigationsystem.cache.CacheService;
import irrigationsystem.dto.*;
import irrigationsystem.mapper.Mapper;
import irrigationsystem.model.*;
import irrigationsystem.repository.*;
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
    private final DeviceRepository deviceRepository;
    private final MqttPublisher mqttPublisher;
    private final CacheService cacheService;
    private final SensorDataService sensorDataService;

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

        Plant plant = createNewPlant(createPlantDto);

        plantRepository.save(plant);

        attachSensors(plant, device);

       /*
       Save again to update sensor references
        */
        plantRepository.save(plant);

        return ResponseDto.<String>builder().value("Plant created successfully").build();
    }

    public ResponseDto<List<PlantReportDto>> getUserPlantReports() {
        Optional<Long> userId = getUserId();

        if (userId.isEmpty()) {
            return ResponseDto.<List<PlantReportDto>>builder().errorMessage(Optional.of("Invalid authentication.")).hasErrors(true).build();
        }

        List<PlantReportDto> plantReport = sensorDataService.getPlantReport(userId.get());

        return ResponseDto.<List<PlantReportDto>>builder().value(plantReport).build();
    }

    public ResponseDto<String> irrigate(long deviceId, int relayId, int irrigationDuration) {
        String topic = "garden/" + deviceId + "/relay/" + relayId;
        mqttPublisher.publish(topic, String.format("{\"relayId\":%d,\"irrigationDuration\":\"%d\"}", relayId, irrigationDuration));

        return ResponseDto.<String>builder().value("Relay ON command sent to device " + deviceId).build();
    }

    private Plant createNewPlant(CreatePlantDto createPlantDto) {
        Plant plant = new Plant();
        plant.setPlantTypeId(createPlantDto.getPlantTypeId());
        plant.setPlantingDate(createPlantDto.getPlantingDate());
        plant.setRelayNumber(1);

        return plant;
    }

    /*
    Every plant has several sensors attached.
    Each sensor has a type and each type can measure one or two parameters
    */
    private void attachSensors(Plant plant, Device device) {

        var sensorTypes = sensorTypeRepository.findAll();

        for (SensorType type : sensorTypes) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(type);
            sensor.setDevice(device);
            sensor.setPlant(plant);
            plant.getSensors().add(sensor);
        }
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
