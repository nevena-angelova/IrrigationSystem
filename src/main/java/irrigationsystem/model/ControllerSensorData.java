package irrigationsystem.model;

import java.time.LocalDateTime;

public interface ControllerSensorData {
    Integer getControllerId();

    Double getAltitude();

    Double getLatitude();

    Integer getSensorId();

    Integer getMeasureTypeId();

    Double getValue();

    LocalDateTime getCreationDate();

    String getMeasureType();
}
