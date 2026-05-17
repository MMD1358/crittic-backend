package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.ConversationCreateDTO;
import daw2.mariomontes.crittic.dtos.ConversationDTO;
import daw2.mariomontes.crittic.services.ConversationService;
import daw2.mariomontes.crittic.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final UserService userService;

    public ConversationController(ConversationService conversationService, UserService userService) {
        this.conversationService = conversationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getMine(Authentication authentication) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.ok(conversationService.getMyConversations(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getById(@PathVariable Integer id, Authentication authentication) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.ok(conversationService.getById(id, userId));
    }

    @PostMapping
    public ResponseEntity<ConversationDTO> create(@Valid @RequestBody ConversationCreateDTO dto, Authentication authentication) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(conversationService.create(userId, dto));
    }
}
