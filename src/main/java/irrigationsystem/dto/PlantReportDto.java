package irrigationsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class PlantReportDto {

    @Setter
    private Long id;

    @Setter
    private LocalDate plantingDate;

    private PlantTypeDto plantType;

    @Setter
    private ReportDto report;

    @Setter
    private Integer deviceId;

    @Setter
    private Integer relayNumber;

    public PlantReportDto(MeasureValuesDto measureValuesDto, ReportDto report) {
        this.id = measureValuesDto.getPlantId();
        this.plantType = new PlantTypeDto(measureValuesDto.getPlantTypeId(), measureValuesDto.getPlantTypeName());
        this.plantingDate = measureValuesDto.getPlantingDate();
        this.deviceId = measureValuesDto.getDeviceId();
        this.relayNumber = measureValuesDto.getRelayNumber();
        this.report = report;
    }
}
