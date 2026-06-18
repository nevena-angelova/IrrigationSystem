package irrigationsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantTypeDto {

    public PlantTypeDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private int id;

    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
