package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ConversationDTO {
    private Integer conversationId;
    private LocalDateTime createdAt;
    private List<UserDTO> participants;
    private MessageDTO lastMessage;
}
