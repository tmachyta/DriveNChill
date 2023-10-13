package carsharing.dto.user;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String password;
    private String repeatPassword;
    private String firstName;
    private String lastName;
}
