package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findByReviewReviewId(Integer reviewId, Pageable pageable);

    long countByReviewReviewId(Integer reviewId);
}