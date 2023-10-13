package carsharing.mapper.user;

import carsharing.config.MapperConfig;
import carsharing.dto.user.UserRegistrationRequest;
import carsharing.dto.user.UserResponseDto;
import carsharing.dto.user.UserResponseRoleDto;
import carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toModel(UserRegistrationRequest request);

    UserResponseDto toUserResponse(User user);

    UserResponseRoleDto toUserRoleResponse(User user);
}
