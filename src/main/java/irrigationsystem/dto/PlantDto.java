package irrigationsystem.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlantDto {

    private Long id;

    private LocalDate plantingDate;

    private PlantTypeDto plantType;

    private ReportDto report;

    private Integer deviceId;

    private Integer relayNumber;

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public PlantTypeDto getPlantType() {
        return plantType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public void setPlantType(Integer PlantTypeId, String PlantTypeName) {
        this.plantType = new PlantTypeDto();
        plantType.setId(PlantTypeId);
        plantType.setName(PlantTypeName);
    }

    public ReportDto getReport() {
        return report;
    }

    public void setReport(ReportDto report) {
        this.report = report;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getRelayNumber(Integer relayNumber) {
        return this.relayNumber;
    }

    public void setRelayNumber(Integer relayNumber) {
        this.relayNumber = relayNumber;
    }
}
