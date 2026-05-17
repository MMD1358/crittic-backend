package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.Videogame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideogameRepository extends JpaRepository<Videogame, Integer> {

    Page<Videogame> findByTitleStartingWithIgnoreCase(String title, Pageable pageable);
}