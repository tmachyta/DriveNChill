package carsharing.service.user;

import carsharing.dto.user.UserRegistrationRequest;
import carsharing.dto.user.UserResponseDto;
import carsharing.dto.user.UserResponseRoleDto;
import carsharing.exception.RegistrationException;
import carsharing.model.Role;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto register(UserRegistrationRequest request) throws RegistrationException;

    List<UserResponseDto> getAll(Pageable pageable);

    void deleteById(Long id);

    UserResponseDto findById(Long id);

    UserResponseRoleDto updateRoleByEmail(String email, Role roleName);

    UserResponseRoleDto updateRoleByUserId(Long id, Role roleName);

    UserResponseDto update(Long id, UserRegistrationRequest requestDto)
            throws RegistrationException;
}
