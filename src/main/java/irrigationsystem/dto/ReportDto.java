package irrigationsystem.dto;

import irrigationsystem.entity.GrowthPhase;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ReportDto {

    @Setter
    @Getter
    private long plantId;

    @Getter
    @Setter
    private String growthPhaseName;

    @Setter
    @Getter
    private String growthPhaseDetails;

    @Setter
    @Getter
    private double soilMoisture;

    @Setter
    @Getter
    private double minSoilMoisture;

    @Setter
    @Getter
    private double maxSoilMoisture;

    @Setter
    @Getter
    private double Temperature;

    @Setter
    @Getter
    private double Humidity;

    @Setter
    @Getter
    private double Light;

    @Setter
    @Getter
    private double cropCoefficient;

    @Getter
    private final List<String> warnings;

    public ReportDto(Long plantId, GrowthPhase growthPhase) {
        this.plantId = plantId;
        this.warnings = new ArrayList<>();
        this.minSoilMoisture = growthPhase.getMinSoilMoisture();
        this.maxSoilMoisture = growthPhase.getMaxSoilMoisture();
        this.growthPhaseName = growthPhase.getName();
        this.growthPhaseDetails = growthPhase.getDetails();
        this.cropCoefficient = growthPhase.getCropCoefficient();
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
}
