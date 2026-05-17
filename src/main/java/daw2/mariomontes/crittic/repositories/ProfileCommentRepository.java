package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.ProfileComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileCommentRepository extends JpaRepository<ProfileComment, Integer> {

    List<ProfileComment> findByProfileUser_UsernameAndParentCommentIsNullOrderByCreatedAtDesc(String username);

    List<ProfileComment> findByParentComment_ProfileCommentIdOrderByCreatedAtAsc(Integer parentCommentId);
}