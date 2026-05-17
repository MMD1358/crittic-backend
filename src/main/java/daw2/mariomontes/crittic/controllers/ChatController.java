package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.ChatMessageCreateDTO;
import daw2.mariomontes.crittic.dtos.ChatMessageDTO;
import daw2.mariomontes.crittic.dtos.ChatSummaryDTO;
import daw2.mariomontes.crittic.services.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<ChatSummaryDTO>> getMyChats() {
        return ResponseEntity.ok(chatService.getMyChats());
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(
            @PathVariable Integer conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(chatService.getMessagesPaged(conversationId, page, size));
    }

    @PostMapping("/start/{username}")
    public ResponseEntity<ChatSummaryDTO> startChat(@PathVariable String username) {
        return ResponseEntity.ok(chatService.startChat(username));
    }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<ChatMessageDTO> sendMessage(
            @PathVariable Integer conversationId,
            @Valid @RequestBody ChatMessageCreateDTO dto
    ) {
        return ResponseEntity.ok(chatService.sendMessage(conversationId, dto));
    }

    @PutMapping("/{conversationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer conversationId) {
        chatService.markAsRead(conversationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unread")
    public ResponseEntity<Boolean> hasUnreadMessages() {
        return ResponseEntity.ok(
                chatService.getMyChats()
                        .stream()
                        .anyMatch(ChatSummaryDTO::getHasUnreadMessages)
        );
    }
}