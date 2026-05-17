package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String image;
    private boolean enabled;
    private Set<String> roles;
}
