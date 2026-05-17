package daw2.mariomontes.crittic.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConversationCreateDTO {
    @NotEmpty
    private List<Long> participantIds;
}
