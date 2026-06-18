package irrigationsystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControllerMetrics {
    private Integer controllerId;
    private double altitude;
    private double latitude;
    private Metrics metrics;

    public ControllerMetrics(Integer controllerId, double altitude, double latitude, Metrics metrics) {
        this.controllerId = controllerId;
        this.altitude = altitude;
        this.latitude = latitude;
        this.metrics = metrics;
    }

}
