package irrigationsystem.service;

import irrigationsystem.dto.*;
import irrigationsystem.mapper.Mapper;
import irrigationsystem.model.*;
import irrigationsystem.repository.*;
import irrigationsystem.sensor.analizer.Analyzer;
import irrigationsystem.sensor.analizer.AnalyzerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CropService {

    private final CropTypeRepository cropTypeRepository;
    private final CropRepository cropRepository;
    private final Mapper mapper;
    private final SensorTypeRepository sensorTypeRepository;
    private final SensorRepository sensorRepository;
    private final SensorDataRepository sensorDataRepository;

    public ResponseDto<List<CropTypeDto>> getCropTypes() {

        var cropTypes = mapper.toCropTypeDtoList(cropTypeRepository.findAll());

        return ResponseDto.<List<CropTypeDto>>builder().value(cropTypes).build();
    }

    public ResponseDto<String> createCrop(CreateCropDto createCropDto) {

        var userId = getUserId();

        if (userId.isEmpty()) {
            return ResponseDto.<String>builder().errorMessage(Optional.of("Invalid authentication.")).hasErrors(true).build();
        }

        Crop crop = new Crop();
        crop.setUserId(userId.get());
        crop.setCropTypeId(createCropDto.getCropTypeId());
        crop.setPlantingDate(createCropDto.getPlantingDate());

        cropRepository.save(crop);

        var sensors = getSensors(crop);

        sensorRepository.saveAll(sensors);

        crop.setSensors(sensors);

        cropRepository.save(crop);

        return ResponseDto.<String>builder().value("Crop created successfully").build();
    }

    public ResponseDto<List<CropDto>> getUserCrops() {
        var userId = getUserId();

        if (userId.isEmpty()) {
            return ResponseDto.<List<CropDto>>builder().errorMessage(Optional.of("Invalid authentication.")).hasErrors(true).build();
        }

        List<MeasureValuesDto> measures = sensorDataRepository.getLatestValuesByUserId(userId.get());

        Map<Long, List<MeasureValuesDto>> measuresByCrop = new HashMap<>();

        /*
        groups measure data by crop id
        each group will have crop -> { measure type 1 data, measure type 2 data ...}
        for types like temperature, humidity and pressure
        */
        for (var measure : measures) {
            if (!measuresByCrop.containsKey(measure.getCropId())) {
                List<MeasureValuesDto> list = new ArrayList<>();
                list.add(measure);
                measuresByCrop.put(measure.getCropId(), list);
            } else {
                measuresByCrop.get(measure.getCropId()).add(measure);
            }
        }

        List<CropDto> crops = new ArrayList<>();

        // get measure values for each group

        for (var group : measuresByCrop.entrySet()) {
            Map<MeasureTypeEnum, Double> measureValues = new HashMap<>();
            for (var measure : group.getValue()) {
                var measureType = MeasureTypeEnum.valueOf(measure.getMeasureType());
                measureValues.put(measureType, measure.getValue());
            }

            // each group values has same crop data so take first item
            var first = group.getValue().getFirst();

            var cropType = CropTypeEnum.getValue(first.getCropTypeId());

            Analyzer analyzer = AnalyzerFactory.CreateAnalyzer(group.getKey(), first.getPlantingDate(), cropType, measureValues);
            ReportDto report = analyzer.analyze();

            var cropDto = createCropDto(first);
            cropDto.setReport(report);

            crops.add(cropDto);
        }
        return ResponseDto.<List<CropDto>>builder().value(crops).build();
    }

    public CropDto createCropDto(MeasureValuesDto measureValuesDto) {

        CropDto cropDto = new CropDto();
        cropDto.setId(measureValuesDto.getCropId());
        cropDto.setCropType(measureValuesDto.getCropTypeId(), measureValuesDto.getCropTypeName());
        cropDto.setPlantingDate(measureValuesDto.getPlantingDate());

        return cropDto;
    }

    // Every crop has a temperature, humidity and a pressure sensors attached
    // Sensor has type and each type can measure one or two parameters
    private List<Sensor> getSensors(Crop crop) {

        var sensorTypes = sensorTypeRepository.findByNameIn(List.of("DHT22", "BMP180"));
        List<Sensor> sensors = new ArrayList<>();

        for (var sensorType : sensorTypes) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(sensorType);
            sensor.setCrop(crop);
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
