package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.MessageCreateDTO;
import daw2.mariomontes.crittic.dtos.MessageDTO;
import daw2.mariomontes.crittic.services.MessageService;
import daw2.mariomontes.crittic.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<MessageDTO>> getByConversation(@RequestParam Integer conversationId, Pageable pageable, Authentication authentication) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.ok(messageService.getByConversation(conversationId, userId, pageable));
    }

    @PostMapping
    public ResponseEntity<MessageDTO> send(@Valid @RequestBody MessageCreateDTO dto, Authentication authentication) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.send(userId, dto));
    }
}
