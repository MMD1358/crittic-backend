package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.ReviewLike;
import daw2.mariomontes.crittic.entities.ReviewLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, ReviewLikeId> {
    long countByReview_ReviewId(Integer reviewId);

}
