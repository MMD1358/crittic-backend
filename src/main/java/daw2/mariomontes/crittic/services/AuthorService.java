package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.AuthorCreateDTO;
import daw2.mariomontes.crittic.dtos.AuthorDTO;
import daw2.mariomontes.crittic.entities.Author;
import daw2.mariomontes.crittic.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDTO> getAll() {
        return authorRepository.findAll().stream().map(this::toDTO).toList();
    }

    public AuthorDTO getById(Integer id) {
        return toDTO(findEntity(id));
    }

    public AuthorDTO create(AuthorCreateDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        return toDTO(authorRepository.save(author));
    }

    public AuthorDTO update(Integer id, AuthorCreateDTO dto) {
        Author author = findEntity(id);
        author.setName(dto.getName());
        return toDTO(authorRepository.save(author));
    }

    public void delete(Integer id) {
        authorRepository.delete(findEntity(id));
    }

    public Author findEntity(Integer id) {
        return authorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("El autor no existe."));
    }

    private AuthorDTO toDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setName(author.getName());
        return dto;
    }
}
