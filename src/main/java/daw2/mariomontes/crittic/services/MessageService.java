package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.MessageCreateDTO;
import daw2.mariomontes.crittic.dtos.MessageDTO;
import daw2.mariomontes.crittic.entities.Message;
import daw2.mariomontes.crittic.repositories.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, ConversationService conversationService, UserService userService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
        this.userService = userService;
    }

    public Page<MessageDTO> getByConversation(Integer conversationId, Long userId, Pageable pageable) {
        conversationService.validateParticipant(conversationId, userId);
        return messageRepository.findByConversation_ConversationIdOrderBySentAtAsc(conversationId, pageable).map(this::toDTO);
    }

    public MessageDTO send(Long userId, MessageCreateDTO dto) {
        conversationService.validateParticipant(dto.getConversationId(), userId);
        Message message = new Message();
        message.setConversation(conversationService.findEntity(dto.getConversationId()));
        message.setSender(userService.findEntityById(userId));
        message.setContent(dto.getContent());
        return toDTO(messageRepository.save(message));
    }

    private MessageDTO toDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setMessageId(message.getMessageId());
        dto.setConversationId(message.getConversation().getConversationId());
        dto.setSenderId(message.getSender().getUserId());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        return dto;
    }
}
