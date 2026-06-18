package irrigationsystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorValues {
    private double temperature;
    private double soilMoisture;
    private double humidity;
    private double light;
}
