package daw2.mariomontes.crittic.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageCreateDTO {

    @NotBlank
    private String content;
}