package irrigationsystem.dto;

import java.time.LocalDate;

public class CreateCropDto {
    private Long cropTypeId;

    private LocalDate plantingDate;

    public Long getCropTypeId() {
        return cropTypeId;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }
}
