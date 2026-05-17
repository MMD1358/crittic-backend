package daw2.mariomontes.crittic.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageCreateDTO {
    @NotNull
    private Integer conversationId;
    @NotBlank
    private String content;
}
