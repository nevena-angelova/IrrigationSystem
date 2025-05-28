package irrigationsystem.sensor.lifecycle;

import lombok.Getter;

@Getter
public class GrowthPhaseInfo {
    private final String growthPhase;
    private final String details;
    private final double minHumidity;
    private final double maxHumidity;

    public GrowthPhaseInfo(String growthPhase, String details, double minMoisture, double maxMoisture) {
        this.growthPhase = growthPhase;
        this.details = details;
        this.minHumidity = minMoisture;
        this.maxHumidity = maxMoisture;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }
}
