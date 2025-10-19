package irrigationsystem.mapper;

import java.util.List;

import irrigationsystem.dto.UserDto;
import irrigationsystem.dto.PlantTypeDto;
import irrigationsystem.model.PlantType;
import irrigationsystem.model.User;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    UserDto toUserDto(User user);
    User fromUserDto(UserDto userDto);

    PlantTypeDto toPlantTypeDto(PlantType PlantType);
    List<PlantTypeDto> toPlantTypeDtoList(List<PlantType> PlantTypes);
}
