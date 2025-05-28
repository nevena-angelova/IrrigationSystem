package irrigationsystem.mapper;

import java.util.List;

import irrigationsystem.dto.UserDto;
import irrigationsystem.dto.CropTypeDto;
import irrigationsystem.model.CropType;
import irrigationsystem.model.User;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    UserDto toUserDto(User user);
    User fromUserDto(UserDto userDto);

    CropTypeDto toCropTypeDto(CropType cropType);
    List<CropTypeDto> toCropTypeDtoList(List<CropType> cropTypes);
}
