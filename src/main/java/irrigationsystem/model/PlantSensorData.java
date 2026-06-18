package irrigationsystem.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface PlantSensorData {
    Long getPlantId();

    Integer getPlantTypeId();

    String getPlantTypeName();

    LocalDate getPlantingDate();

    Integer getAreaNumber();

    Long getSensorId();

    String getMeasureType();

    Integer getMeasureTypeId();

    Double getValue();

    OffsetDateTime getCreationDate();

    Integer getControllerId();
}
