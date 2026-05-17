package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.UserFavoriteVideogame;
import daw2.mariomontes.crittic.entities.UserFavoriteVideogameId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteVideogameRepository extends JpaRepository<UserFavoriteVideogame, UserFavoriteVideogameId> {

    List<UserFavoriteVideogame> findByUser_Username(String username);
}