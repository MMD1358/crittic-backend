package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.ProfileCommentLike;
import daw2.mariomontes.crittic.entities.ProfileCommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileCommentLikeRepository extends JpaRepository<ProfileCommentLike, ProfileCommentLikeId> {

    long countByProfileComment_ProfileCommentId(Integer profileCommentId);
}