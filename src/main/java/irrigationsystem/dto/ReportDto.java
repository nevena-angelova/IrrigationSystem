package irrigationsystem.dto;

import irrigationsystem.model.GrowthPhase;

import java.util.ArrayList;
import java.util.List;

public class ReportDto {

    private long PlantId;

    private boolean needsIrrigation;

    private double humidity;

    private String growthPhaseName;

    private String growthPhaseDetails;

    private double minHumidity;

    private double maxHumidity;

    private int irrigationDuration;

    private List<String> warnings;

    public ReportDto(Long PlantId, double humidity, GrowthPhase growthPhase) {
        this.PlantId = PlantId;
        this.needsIrrigation = false;
        this.warnings = new ArrayList<>();
        this.humidity = humidity;
        this.growthPhaseName = growthPhase.getName();
        this.growthPhaseDetails = growthPhase.getDetails();
        this.minHumidity = growthPhase.getMinHumidity();
        this.maxHumidity = growthPhase.getMaxHumidity();
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
        return PlantId;
    }

    public void setPlantId(long PlantId) {
        this.PlantId = PlantId;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
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

    public double getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(double minHumidity) {
        this.minHumidity = minHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public int getIrrigationDuration() {
        return irrigationDuration;
    }

    public void setIrrigationDuration(int irrigationDuration) {
        this.irrigationDuration = irrigationDuration;
    }


}
