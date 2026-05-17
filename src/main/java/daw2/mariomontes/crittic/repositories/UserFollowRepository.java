package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.UserFollow;
import daw2.mariomontes.crittic.entities.UserFollowId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowId> {

    long countByFollowed_UserId(Long userId);

    long countByFollower_UserId(Long userId);
}