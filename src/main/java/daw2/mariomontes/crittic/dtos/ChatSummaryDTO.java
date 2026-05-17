package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatSummaryDTO {
    private Integer conversationId;

    private Long otherUserId;
    private String otherUsername;
    private String otherUserImage;

    private String lastMessage;
    private LocalDateTime lastMessageAt;

    private Boolean hasUnreadMessages;
}