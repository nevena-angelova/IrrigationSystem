package irrigationsystem.dto;

import irrigationsystem.model.GrowthPhase;

import java.util.ArrayList;
import java.util.List;

public class ReportDto {

    private long plantId;

    private boolean needsIrrigation;

    private String growthPhaseName;

    private String growthPhaseDetails;

    private double soilMoisture;

    private double minSoilMoisture;

    private double maxSoilMoisture;

    private int irrigationDuration;

    private List<String> warnings;

    public ReportDto(Long plantId, double soilMoisture, GrowthPhase growthPhase) {
        this.plantId = plantId;
        this.needsIrrigation = false;
        this.warnings = new ArrayList<>();
        this.soilMoisture = soilMoisture;
        this.minSoilMoisture = growthPhase.getMinSoilMoisture();
        this.maxSoilMoisture = growthPhase.getMaxSoilMoisture();
        this.growthPhaseName = growthPhase.getName();
        this.growthPhaseDetails = growthPhase.getDetails();
        this.irrigationDuration = growthPhase.getIrrigationDuration();
    }

    public boolean getNeedsIrrigation() {
        return needsIrrigation;
    }

    public void setNeedsIrrigation(boolean needsIrrigation) {
        this.needsIrrigation = needsIrrigation;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long PlantId) {
        this.plantId = PlantId;
    }

    public double getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(double soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public String getGrowthPhaseName() {
      return growthPhaseName;
    }

    public void getGrowthPhaseName(String growthPhase) {
      this.growthPhaseName = growthPhaseName;
    }

    public String getGrowthPhaseDetails() {
        return growthPhaseDetails;
    }

    public void setGrowthPhaseDetails(String growthPhaseDetails) {
        this.growthPhaseDetails = growthPhaseDetails;
    }

    public double getMinSoilMoisture() {
        return minSoilMoisture;
    }

    public void setMinSoilMoisture(double minSoilMoisture) {
        this.minSoilMoisture = minSoilMoisture;
    }

    public double getMaxSoilMoisture() {
        return maxSoilMoisture;
    }

    public void setMaxSoilMoisture(double maxSoilMoisture) {
        this.maxSoilMoisture = maxSoilMoisture;
    }

    public int getIrrigationDuration() {
        return irrigationDuration;
    }

    public void setIrrigationDuration(int irrigationDuration) {
        this.irrigationDuration = irrigationDuration;
    }


}
