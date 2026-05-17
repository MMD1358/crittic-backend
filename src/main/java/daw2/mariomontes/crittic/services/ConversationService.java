package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.ConversationCreateDTO;
import daw2.mariomontes.crittic.dtos.ConversationDTO;
import daw2.mariomontes.crittic.dtos.MessageDTO;
import daw2.mariomontes.crittic.dtos.UserDTO;
import daw2.mariomontes.crittic.entities.Conversation;
import daw2.mariomontes.crittic.entities.ConversationUser;
import daw2.mariomontes.crittic.entities.ConversationUserId;
import daw2.mariomontes.crittic.mappers.UserMapper;
import daw2.mariomontes.crittic.repositories.ConversationRepository;
import daw2.mariomontes.crittic.repositories.ConversationUserRepository;
import daw2.mariomontes.crittic.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationUserRepository conversationUserRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    public ConversationService(ConversationRepository conversationRepository, ConversationUserRepository conversationUserRepository, MessageRepository messageRepository, UserService userService, UserMapper userMapper) {
        this.conversationRepository = conversationRepository;
        this.conversationUserRepository = conversationUserRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public List<ConversationDTO> getMyConversations(Long userId) {
        return conversationUserRepository.findByUser_UserId(userId).stream()
                .map(ConversationUser::getConversation)
                .distinct()
                .map(this::toDTO)
                .toList();
    }

    public ConversationDTO getById(Integer id, Long userId) {
        validateParticipant(id, userId);
        return toDTO(findEntity(id));
    }

    public ConversationDTO create(Long currentUserId, ConversationCreateDTO dto) {
        LinkedHashSet<Long> ids = new LinkedHashSet<>(dto.getParticipantIds());
        ids.add(currentUserId);
        Conversation conversation = conversationRepository.save(new Conversation());
        for (Long participantId : ids) {
            ConversationUser conversationUser = new ConversationUser();
            conversationUser.setId(new ConversationUserId(conversation.getConversationId(), participantId));
            conversationUser.setConversation(conversation);
            conversationUser.setUser(userService.findEntityById(participantId));
            conversationUserRepository.save(conversationUser);
        }
        return toDTO(conversation);
    }

    public Conversation findEntity(Integer id) {
        return conversationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("La conversación no existe."));
    }

    public void validateParticipant(Integer conversationId, Long userId) {
        if (!conversationUserRepository.existsByConversation_ConversationIdAndUser_UserId(conversationId, userId)) {
            throw new IllegalArgumentException("No perteneces a esta conversación.");
        }
    }

    private ConversationDTO toDTO(Conversation conversation) {
        ConversationDTO dto = new ConversationDTO();
        dto.setConversationId(conversation.getConversationId());
        dto.setCreatedAt(conversation.getCreatedAt());
        List<UserDTO> participants = new ArrayList<>();
        for (ConversationUser conversationUser : conversationUserRepository.findByConversation_ConversationId(conversation.getConversationId())) {
            participants.add(userMapper.toDTO(conversationUser.getUser()));
        }
        dto.setParticipants(participants);
        MessageDTO lastMessage = messageRepository.findTopByConversation_ConversationIdOrderBySentAtDesc(conversation.getConversationId())
                .map(message -> {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setMessageId(message.getMessageId());
                    messageDTO.setConversationId(message.getConversation().getConversationId());
                    messageDTO.setSenderId(message.getSender().getUserId());
                    messageDTO.setSenderUsername(message.getSender().getUsername());
                    messageDTO.setContent(message.getContent());
                    messageDTO.setSentAt(message.getSentAt());
                    return messageDTO;
                })
                .orElse(null);
        dto.setLastMessage(lastMessage);
        return dto;
    }
}
