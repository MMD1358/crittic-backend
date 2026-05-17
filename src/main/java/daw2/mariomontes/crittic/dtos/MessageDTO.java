package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDTO {
    private Integer messageId;
    private Integer conversationId;
    private Long senderId;
    private String senderUsername;
    private String content;
    private LocalDateTime sentAt;
}
