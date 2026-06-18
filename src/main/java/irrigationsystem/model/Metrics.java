package irrigationsystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metrics {
    private final double tMin;
    private final double tMax;
    private final double tMean;
    private final double rhMin;
    private final double rhMax;

    public Metrics(double tMin, double tMax, double tMean,
                   double rhMin, double rhMax) {
        this.tMin = tMin;
        this.tMax = tMax;
        this.tMean = tMean;
        this.rhMin = rhMin;
        this.rhMax = rhMax;
    }
}
