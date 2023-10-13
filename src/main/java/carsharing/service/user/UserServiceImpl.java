package carsharing.service.user;

import carsharing.dto.user.UserRegistrationRequest;
import carsharing.dto.user.UserResponseDto;
import carsharing.dto.user.UserResponseRoleDto;
import carsharing.exception.EntityNotFoundException;
import carsharing.exception.RegistrationException;
import carsharing.mapper.user.UserMapper;
import carsharing.model.Role;
import carsharing.model.Role.RoleName;
import carsharing.model.User;
import carsharing.repository.role.RoleRepository;
import carsharing.repository.user.UserRepository;
import carsharing.service.role.RoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequest request) throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }

        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new RegistrationException("Passwords do not match");
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleService.getRoleByRoleName(RoleName.CUSTOMER);
        user.setRoles(new HashSet<>(Set.of(userRole)));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by id " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseRoleDto updateRoleByEmail(String email, Role roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        Role role = roleRepository.findRoleByRoleName(roleName.getRoleName())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find role by roleName " + roleName));
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        return userMapper.toUserRoleResponse(userRepository.save(user));
    }

    @Override
    public UserResponseRoleDto updateRoleByUserId(Long id, Role roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by id " + id));
        Role role = roleRepository.findRoleByRoleName(roleName.getRoleName())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find role by roleName " + roleName));
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        return userMapper.toUserRoleResponse(userRepository.save(user));
    }

    @Override
    public UserResponseDto update(Long id, UserRegistrationRequest requestDto)
            throws RegistrationException {
        User existedUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by id " + id));
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new RegistrationException("Passwords do not match");
        }
        existedUser.setEmail(requestDto.getEmail());
        existedUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        existedUser.setFirstName(requestDto.getFirstName());
        existedUser.setLastName(requestDto.getLastName());
        return userMapper.toDto(userRepository.save(existedUser));
    }
}

