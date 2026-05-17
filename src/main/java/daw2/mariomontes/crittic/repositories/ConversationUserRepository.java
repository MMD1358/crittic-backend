package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.ConversationUser;
import daw2.mariomontes.crittic.entities.ConversationUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationUserRepository extends JpaRepository<ConversationUser, ConversationUserId> {
    List<ConversationUser> findByUser_UserId(Long userId);
    List<ConversationUser> findByConversation_ConversationId(Integer conversationId);
    boolean existsByConversation_ConversationIdAndUser_UserId(Integer conversationId, Long userId);
}
