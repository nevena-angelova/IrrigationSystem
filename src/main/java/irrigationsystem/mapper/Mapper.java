package irrigationsystem.mapper;

import java.util.List;

import irrigationsystem.dto.UserDto;
import irrigationsystem.dto.PlantTypeDto;
import irrigationsystem.entity.PlantType;
import irrigationsystem.entity.User;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    PlantTypeDto toPlantTypeDto(PlantType PlantType);
    List<PlantTypeDto> toPlantTypeDtoList(List<PlantType> PlantTypes);
}
