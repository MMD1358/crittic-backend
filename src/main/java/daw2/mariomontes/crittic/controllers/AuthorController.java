package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.AuthorCreateDTO;
import daw2.mariomontes.crittic.dtos.AuthorDTO;
import daw2.mariomontes.crittic.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAll() {
        return ResponseEntity.ok(authorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(authorService.getById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@Valid @RequestBody AuthorCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> update(@PathVariable Integer id, @Valid @RequestBody AuthorCreateDTO dto) {
        return ResponseEntity.ok(authorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
