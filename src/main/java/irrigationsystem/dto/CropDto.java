package irrigationsystem.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CropDto {

    private Long id;

    private LocalDate plantingDate;

    private CropTypeDto cropType;

    private ReportDto report;

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public CropTypeDto getCropType() {
        return cropType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public void setCropType(Integer cropTypeId, String cropTypeName) {
        this.cropType = new CropTypeDto();
        cropType.setId(cropTypeId);
        cropType.setName(cropTypeName);
    }

    public ReportDto getReport() {
        return report;
    }

    public void setReport(ReportDto report) {
        this.report = report;
    }
}
