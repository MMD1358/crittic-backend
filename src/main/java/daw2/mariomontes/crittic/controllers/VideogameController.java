package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.RatingDTO;
import daw2.mariomontes.crittic.dtos.VideogameCreateDTO;
import daw2.mariomontes.crittic.dtos.VideogameDTO;
import daw2.mariomontes.crittic.services.VideogameService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/videogames")
public class VideogameController {

    private final VideogameService videogameService;

    public VideogameController(VideogameService videogameService) {
        this.videogameService = videogameService;
    }

    @GetMapping
    public ResponseEntity<Page<VideogameDTO>> getAll(@RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(videogameService.getAll(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideogameDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(videogameService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VideogameDTO> create(@Valid @RequestBody VideogameCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videogameService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideogameDTO> update(@PathVariable Integer id, @Valid @RequestBody VideogameCreateDTO dto) {
        return ResponseEntity.ok(videogameService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        videogameService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<VideogameDTO> like(@PathVariable Integer id) {
        return ResponseEntity.ok(videogameService.like(id));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<VideogameDTO> unlike(@PathVariable Integer id) {
        return ResponseEntity.ok(videogameService.unlike(id));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<VideogameDTO> rateVideogame(
            @PathVariable Integer id,
            @RequestBody RatingDTO ratingDTO
    ) {
        return ResponseEntity.ok(videogameService.rateVideogame(id, ratingDTO.getRating()));
    }
}
