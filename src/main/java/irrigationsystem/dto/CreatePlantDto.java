package irrigationsystem.dto;

import java.time.LocalDate;

public class CreatePlantDto {
    private int plantTypeId;

    private LocalDate plantingDate;

    public int getPlantTypeId() {
        return plantTypeId;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }
}
