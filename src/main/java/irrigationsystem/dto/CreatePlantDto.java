package irrigationsystem.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreatePlantDto {
    private int plantTypeId;

    private LocalDate plantingDate;
}
