package carsharing.controller;

import carsharing.dto.user.UserRegistrationRequest;
import carsharing.dto.user.UserResponseDto;
import carsharing.dto.user.UserResponseRoleDto;
import carsharing.exception.RegistrationException;
import carsharing.model.Role;
import carsharing.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @GetMapping
    @Operation(summary = "Get all users", description = "Get a list of all available users")
    public List<UserResponseDto> findAll(@ParameterObject Pageable pageable) {
        return userService.getAll(pageable);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id", description = "Soft delete of available user by id")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @GetMapping("/me/{id}")
    @Operation(summary = "Get user by id", description = "Get available user by id")
    public UserResponseDto getBookById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    @PutMapping("/me/{id}")
    @Operation(summary = "Update user by id", description = "update available user by id")
    public UserResponseDto updateUserInfo(@PathVariable Long id,
                                          @RequestBody @Valid UserRegistrationRequest request)
            throws RegistrationException {
        return userService.update(id, request);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/role/{id}")
    @Operation(summary = "Update user role by id",
            description = "Update available user role by id")
    public UserResponseRoleDto updateUserRoleById(@PathVariable Long id,
                                                  @RequestBody Role roleName) {
        return userService.updateRoleByUserId(id, roleName);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/role/{email}")
    @Operation(summary = "Update user role by email",
            description = "Update available user role by email")
    public UserResponseRoleDto updateUserRoleEmail(@PathVariable String email,
                                              @RequestBody Role roleName) {
        return userService.updateRoleByEmail(email, roleName);
    }
}
