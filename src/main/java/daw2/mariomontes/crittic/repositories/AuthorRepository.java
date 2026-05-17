package daw2.mariomontes.crittic.repositories;

import daw2.mariomontes.crittic.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
