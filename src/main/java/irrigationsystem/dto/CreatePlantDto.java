package irrigationsystem.dto;

import java.time.LocalDate;

public class CreatePlantDto {
    private Long plantTypeId;

    private LocalDate plantingDate;

    public Long getPlantTypeId() {
        return plantTypeId;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }
}
