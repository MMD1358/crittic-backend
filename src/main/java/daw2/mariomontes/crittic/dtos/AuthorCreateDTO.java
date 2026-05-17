package daw2.mariomontes.crittic.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorCreateDTO {
    @NotBlank
    @Size(max = 100)
    private String name;
}
