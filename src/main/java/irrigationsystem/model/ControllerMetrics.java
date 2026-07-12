package irrigationsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ControllerMetrics {
    private Integer controllerId;
    private double altitude;
    private double latitude;
    private Metrics metrics;
    private List<LightData> lightData;

    public ControllerMetrics(Integer controllerId, double altitude, double latitude, List<LightData> lightData, Metrics metrics) {
        this.controllerId = controllerId;
        this.altitude = altitude;
        this.latitude = latitude;
        this.metrics = metrics;
        this.lightData = lightData;
    }
}
