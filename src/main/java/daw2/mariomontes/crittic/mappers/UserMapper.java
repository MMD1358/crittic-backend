package daw2.mariomontes.crittic.mappers;

import daw2.mariomontes.crittic.dtos.UserDTO;
import daw2.mariomontes.crittic.entities.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setImage(user.getImage());
        dto.setEnabled(user.isEnabled());
        dto.setRoles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));
        return dto;
    }
}
