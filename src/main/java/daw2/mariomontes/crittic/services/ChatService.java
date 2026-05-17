package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.ChatMessageCreateDTO;
import daw2.mariomontes.crittic.dtos.ChatMessageDTO;
import daw2.mariomontes.crittic.dtos.ChatSummaryDTO;
import daw2.mariomontes.crittic.entities.*;
import daw2.mariomontes.crittic.repositories.ConversationRepository;
import daw2.mariomontes.crittic.repositories.ConversationUserRepository;
import daw2.mariomontes.crittic.repositories.MessageRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final ConversationUserRepository conversationUserRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;

    public ChatService(
            ConversationRepository conversationRepository,
            ConversationUserRepository conversationUserRepository,
            MessageRepository messageRepository,
            UserService userService
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationUserRepository = conversationUserRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public List<ChatSummaryDTO> getMyChats() {
        User currentUser = getCurrentUser();

        return conversationUserRepository.findByUser_UserId(currentUser.getUserId())
                .stream()
                .map(conversationUser -> toChatSummaryDTO(
                        conversationUser.getConversation(),
                        currentUser
                ))
                .sorted(Comparator.comparing(
                        ChatSummaryDTO::getLastMessageAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();
    }

    public List<ChatMessageDTO> getMessages(Integer conversationId) {
        User currentUser = getCurrentUser();

        checkUserBelongsToConversation(conversationId, currentUser.getUserId());

        return messageRepository
                .findByConversation_ConversationIdOrderBySentAtAsc(conversationId)
                .stream()
                .map(message -> toChatMessageDTO(message, currentUser))
                .toList();
    }

    public ChatSummaryDTO startChat(String username) {
        User currentUser = getCurrentUser();
        User otherUser = userService.findEntityByUsername(username);

        if (currentUser.getUserId().equals(otherUser.getUserId())) {
            throw new IllegalArgumentException("You cannot start a chat with yourself.");
        }

        Conversation conversation = conversationRepository
                .findConversationBetweenUsers(currentUser.getUserId(), otherUser.getUserId())
                .orElseGet(() -> createConversation(currentUser, otherUser));

        return toChatSummaryDTO(conversation, currentUser);
    }

    public ChatMessageDTO sendMessage(Integer conversationId, ChatMessageCreateDTO dto) {
        User currentUser = getCurrentUser();

        checkUserBelongsToConversation(conversationId, currentUser.getUserId());

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found."));

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(currentUser);
        message.setContent(dto.getContent());

        return toChatMessageDTO(messageRepository.save(message), currentUser);
    }

    public void markAsRead(Integer conversationId) {
        User currentUser = getCurrentUser();

        checkUserBelongsToConversation(conversationId, currentUser.getUserId());

        List<Message> unreadMessages =
                messageRepository.findByConversation_ConversationIdAndSender_UserIdNotAndReadAtIsNull(
                        conversationId,
                        currentUser.getUserId()
                );

        unreadMessages.forEach(message -> message.setReadAt(LocalDateTime.now()));

        messageRepository.saveAll(unreadMessages);
    }

    private Conversation createConversation(User currentUser, User otherUser) {
        Conversation conversation = conversationRepository.save(new Conversation());

        ConversationUser currentConversationUser = new ConversationUser();
        currentConversationUser.setId(
                new ConversationUserId(conversation.getConversationId(), currentUser.getUserId())
        );
        currentConversationUser.setConversation(conversation);
        currentConversationUser.setUser(currentUser);

        ConversationUser otherConversationUser = new ConversationUser();
        otherConversationUser.setId(
                new ConversationUserId(conversation.getConversationId(), otherUser.getUserId())
        );
        otherConversationUser.setConversation(conversation);
        otherConversationUser.setUser(otherUser);

        conversationUserRepository.save(currentConversationUser);
        conversationUserRepository.save(otherConversationUser);

        return conversation;
    }

    private ChatSummaryDTO toChatSummaryDTO(Conversation conversation, User currentUser) {
        User otherUser = getOtherUser(conversation.getConversationId(), currentUser.getUserId());

        Message lastMessage = messageRepository
                .findTopByConversation_ConversationIdOrderBySentAtDesc(conversation.getConversationId())
                .orElse(null);

        ChatSummaryDTO dto = new ChatSummaryDTO();

        dto.setConversationId(conversation.getConversationId());

        dto.setOtherUserId(otherUser.getUserId());
        dto.setOtherUsername(otherUser.getUsername());
        dto.setOtherUserImage(otherUser.getImage());

        if (lastMessage != null) {
            dto.setLastMessage(lastMessage.getContent());
            dto.setLastMessageAt(lastMessage.getSentAt());
        }

        dto.setHasUnreadMessages(
                messageRepository.countByConversation_ConversationIdAndSender_UserIdNotAndReadAtIsNull(
                        conversation.getConversationId(),
                        currentUser.getUserId()
                ) > 0
        );

        return dto;
    }

    private ChatMessageDTO toChatMessageDTO(Message message, User currentUser) {
        ChatMessageDTO dto = new ChatMessageDTO();

        dto.setMessageId(message.getMessageId());
        dto.setConversationId(message.getConversation().getConversationId());

        dto.setSenderId(message.getSender().getUserId());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setSenderImage(message.getSender().getImage());

        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());

        dto.setReadByCurrentUser(
                message.getSender().getUserId().equals(currentUser.getUserId())
                        || message.getReadAt() != null
        );

        return dto;
    }

    private User getOtherUser(Integer conversationId, Long currentUserId) {
        return conversationUserRepository
                .findByConversation_ConversationId(conversationId)
                .stream()
                .map(ConversationUser::getUser)
                .filter(user -> !user.getUserId().equals(currentUserId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Other user not found."));
    }

    private void checkUserBelongsToConversation(Integer conversationId, Long userId) {
        boolean belongs = conversationUserRepository
                .findByConversation_ConversationId(conversationId)
                .stream()
                .anyMatch(conversationUser -> conversationUser.getUser().getUserId().equals(userId));

        if (!belongs) {
            throw new IllegalArgumentException("You do not belong to this conversation.");
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (
                authentication == null ||
                        !authentication.isAuthenticated() ||
                        authentication.getName().equals("anonymousUser")
        ) {
            throw new IllegalArgumentException("You must log in.");
        }

        return userService.findEntityByUsername(authentication.getName());
    }

    public List<ChatMessageDTO> getMessagesPaged(Integer conversationId, int page, int size) {
        User currentUser = getCurrentUser();

        checkUserBelongsToConversation(conversationId, currentUser.getUserId());

        return messageRepository
                .findByConversation_ConversationIdOrderBySentAtDesc(
                        conversationId,
                        org.springframework.data.domain.PageRequest.of(page, size)
                )
                .getContent()
                .stream()
                .sorted(java.util.Comparator.comparing(Message::getSentAt))
                .map(message -> toChatMessageDTO(message, currentUser))
                .toList();
    }
}