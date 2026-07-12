package irrigationsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LightData {
    private double value;
    private LocalDateTime creationDate;

    public LightData(Double value, LocalDateTime creationDate) {
        this.value = value;
        this.creationDate = creationDate;
    }
}
