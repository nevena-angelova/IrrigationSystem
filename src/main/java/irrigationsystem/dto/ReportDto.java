package irrigationsystem.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportDto {

    private long cropId;

    private boolean needsIrrigation;

    private double humidity;

    private double minHumidity;

    private double maxHumidity;

    private List<String> warnings;

    public ReportDto(Long cropId, double humidity, double minHumidity, double maxHumidity) {
        this.cropId = cropId;
        this.needsIrrigation = false;
        this.warnings = new ArrayList<>();
        this.humidity = humidity;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
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

    public long getCropId() {
        return cropId;
    }

    public void setCropId(long cropId) {
        this.cropId = cropId;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setMinHumidity(double minHumidity) {
        this.minHumidity = minHumidity;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public void setMaxHumidity(double maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }
}
