package daw2.mariomontes.crittic.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDTO {
    @NotNull
    private Integer reviewId;
    @NotBlank
    private String content;
}
