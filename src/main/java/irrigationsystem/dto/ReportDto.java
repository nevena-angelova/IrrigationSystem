package irrigationsystem.dto;

import irrigationsystem.model.GrowthPhase;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ReportDto {

    @Setter
    @Getter
    private long plantId;

    @Setter
    @Getter
    private boolean needsIrrigation;

    @Setter
    @Getter
    private boolean hasWater;

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
    private int irrigationDuration;

    @Getter
    private List<String> warnings;

    public ReportDto(Long plantId, GrowthPhase growthPhase, boolean hasWater) {
        this.plantId = plantId;
        this.needsIrrigation = false;
        this.hasWater = hasWater;
        this.warnings = new ArrayList<>();
        this.minSoilMoisture = growthPhase.getMinSoilMoisture();
        this.maxSoilMoisture = growthPhase.getMaxSoilMoisture();
        this.growthPhaseName = growthPhase.getName();
        this.growthPhaseDetails = growthPhase.getDetails();
        this.irrigationDuration = growthPhase.getIrrigationDuration();
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
}
