package irrigationsystem.service;

import irrigationsystem.analyzer.Analyzer;
import irrigationsystem.analyzer.AnalyzerFactory;
import irrigationsystem.cache.CacheService;
import irrigationsystem.dto.MeasureValuesDto;
import irrigationsystem.dto.PlantReportDto;
import irrigationsystem.dto.ReportDto;
import irrigationsystem.model.GrowthPhase;
import irrigationsystem.model.MeasureTypeEnum;
import irrigationsystem.model.PlantType;
import irrigationsystem.repository.SensorDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final CacheService cacheService;

    public SensorDataService(SensorDataRepository sensorDataRepository, CacheService cacheService) {
        this.sensorDataRepository = sensorDataRepository;
        this.cacheService = cacheService;
    }

    public List<PlantReportDto> getPlantReport(Long userId) {
        List<MeasureValuesDto> measures = sensorDataRepository.getLatestValuesByUserId(userId);

        return createPlantReport(measures);
    }

    public List<PlantReportDto> getAllPlantReports() {
        List<MeasureValuesDto> measures = sensorDataRepository.getLatestValuesAllUsers();

        return createPlantReport(measures);
    }

    /*
    Group measures by Plant ID, then for each group create a PlantReportDto with the latest measure values and an analysis report.
    */
    public List<PlantReportDto> createPlantReport(List<MeasureValuesDto> measures) {
        return measures.stream()
            .collect(Collectors.groupingBy(MeasureValuesDto::getPlantId))
            .values()
            .stream()
            .map(this::analyzePlantData)
            .toList();
    }

    private PlantReportDto analyzePlantData(List<MeasureValuesDto> group) {

        MeasureValuesDto value = group.getFirst();

        GrowthPhase growthPhase = cacheService.getGrowthPhase(value.getPlantingDate(), value.getPlantTypeId());
        PlantType plantType = cacheService.getPlantType(value.getPlantTypeId());

        Map<MeasureTypeEnum, Double> measureValues =
            group.stream().collect(Collectors.toMap(
                v -> MeasureTypeEnum.valueOf(v.getMeasureType()),
                MeasureValuesDto::getValue
            ));

        Analyzer analyzer = AnalyzerFactory.createAnalyzer(
            value.getPlantId(),
            plantType,
            growthPhase,
            measureValues
        );

        ReportDto report = analyzer.analyze();

        return new PlantReportDto(value, report);
    }
}
