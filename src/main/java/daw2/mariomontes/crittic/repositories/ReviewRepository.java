package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.Review;
import daw2.mariomontes.crittic.entities.User;
import daw2.mariomontes.crittic.entities.Videogame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findByVideogame_VideogameId(Integer videogameId, Pageable pageable);
    Page<Review> findByUser_UserId(Long userId, Pageable pageable);
    Optional<Review> findByUserAndVideogame(User user, Videogame videogame);
    long countByVideogame_VideogameId(Integer videogameId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.videogame.videogameId = :videogameId")
    Double getAverageRatingByVideogameId(@Param("videogameId") Integer videogameId);
    Optional<Review> findByUser_UserIdAndVideogame_VideogameId(Long userId, Integer videogameId);
}
