package irrigationsystem.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface MeasureValuesDto {
    Long getPlantId();
    Integer getPlantTypeId();
    String getPlantTypeName();
    LocalDate getPlantingDate();
    Long getSensorId();
    String getMeasureType();
    Integer getMeasureTypeId();
    Double getValue();
    OffsetDateTime getTs();
    Integer getDeviceId();
    Integer getRelayId();
}
