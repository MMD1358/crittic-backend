package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    @Query("""
        SELECT c
        FROM Conversation c
        JOIN ConversationUser cu1 ON cu1.conversation = c
        JOIN ConversationUser cu2 ON cu2.conversation = c
        WHERE cu1.user.userId = :userId1
        AND cu2.user.userId = :userId2
    """)
    Optional<Conversation> findConversationBetweenUsers(Long userId1, Long userId2);
}