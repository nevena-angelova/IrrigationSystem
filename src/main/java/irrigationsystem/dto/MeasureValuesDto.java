package irrigationsystem.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public interface MeasureValuesDto {
    Long getCropId();
    Integer getCropTypeId();
    String getCropTypeName();
    LocalDate getPlantingDate();
    Long getSensorId();
    String getMeasureType();
    Integer getMeasureTypeId();
    Double getValue();
    OffsetDateTime getTs();
}
