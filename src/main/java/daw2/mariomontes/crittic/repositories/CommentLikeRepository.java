package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.CommentLike;
import daw2.mariomontes.crittic.entities.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {

    long countByCommentCommentId(Integer commentId);
}