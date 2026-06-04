package irrigationsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class PlantDto {

    @Setter
    private Long id;

    @Setter
    private LocalDate plantingDate;

    private PlantTypeDto plantType;

    @Setter
    private ReportDto report;

    @Setter
    private Integer deviceId;

    @Setter
    private Integer relayNumber;

  public void setPlantType(Integer PlantTypeId, String PlantTypeName) {
        this.plantType = new PlantTypeDto();
        plantType.setId(PlantTypeId);
        plantType.setName(PlantTypeName);
    }

  public Integer getRelayNumber(Integer relayNumber) {
        return this.relayNumber;
    }

}
