package carsharing.dto.user;

import carsharing.model.Role;
import java.util.Set;
import lombok.Data;

@Data
public class UserResponseRoleDto {
    private Set<Role> roles;
}
