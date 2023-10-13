package carsharing.service.role;

import carsharing.model.Role;
import carsharing.model.Role.RoleName;

public interface RoleService {
    Role getRoleByRoleName(RoleName roleName);
}
