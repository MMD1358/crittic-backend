package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.VideogameLike;
import daw2.mariomontes.crittic.entities.VideogameLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideogameLikeRepository extends JpaRepository<VideogameLike, VideogameLikeId> {
    long countByVideogame_VideogameId(Integer videogameId);
}