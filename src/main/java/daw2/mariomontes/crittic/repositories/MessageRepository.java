package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByConversation_ConversationIdOrderBySentAtAsc(Integer conversationId);

    Page<Message> findByConversation_ConversationIdOrderBySentAtAsc(
            Integer conversationId,
            Pageable pageable
    );

    Page<Message> findByConversation_ConversationIdOrderBySentAtDesc(
            Integer conversationId,
            Pageable pageable
    );

    Optional<Message> findTopByConversation_ConversationIdOrderBySentAtDesc(Integer conversationId);

    long countByConversation_ConversationIdAndSender_UserIdNotAndReadAtIsNull(
            Integer conversationId,
            Long userId
    );

    List<Message> findByConversation_ConversationIdAndSender_UserIdNotAndReadAtIsNull(
            Integer conversationId,
            Long userId
    );
}