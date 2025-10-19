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

    private Integer relayId;

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

    public Integer getRelayId(Integer relayId) {
        return this.relayId;
    }

    public void setRelayId(Integer relayId) {
        this.relayId = relayId;
    }
}
