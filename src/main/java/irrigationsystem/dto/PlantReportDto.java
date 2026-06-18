package irrigationsystem.dto;

import irrigationsystem.model.PlantSensorData;
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
    private Integer controllerId;

    @Setter
    private Integer relayNumber;

    public PlantReportDto(PlantSensorData plantSensorDataDto, ReportDto report) {
        this.id = plantSensorDataDto.getPlantId();
        this.plantType = new PlantTypeDto(plantSensorDataDto.getPlantTypeId(), plantSensorDataDto.getPlantTypeName());
        this.plantingDate = plantSensorDataDto.getPlantingDate();
        this.controllerId = plantSensorDataDto.getControllerId();
        this.relayNumber = plantSensorDataDto.getAreaNumber();
        this.report = report;
    }
}
