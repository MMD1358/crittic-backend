package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.ReviewCreateDTO;
import daw2.mariomontes.crittic.dtos.ReviewDTO;
import daw2.mariomontes.crittic.services.ReviewLikeService;
import daw2.mariomontes.crittic.services.ReviewService;
import daw2.mariomontes.crittic.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;
    private final UserService userService;

    public ReviewController(
            ReviewService reviewService,
            ReviewLikeService reviewLikeService,
            UserService userService
    ) {
        this.reviewService = reviewService;
        this.reviewLikeService = reviewLikeService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<ReviewDTO>> getAll(
            @RequestParam(required = false) Integer videogameId,
            @RequestParam(required = false) Long userId,
            Pageable pageable
    ) {
        if (videogameId != null) {
            return ResponseEntity.ok(reviewService.getByVideogame(videogameId, pageable));
        }

        if (userId != null) {
            return ResponseEntity.ok(reviewService.getByUser(userId, pageable));
        }

        return ResponseEntity.ok(reviewService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @GetMapping("/videogame/{videogameId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByVideogame(
            @PathVariable Integer videogameId
    ) {
        return ResponseEntity.ok(reviewService.getReviewsByVideogame(videogameId));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createOrUpdateReview(
            @Valid @RequestBody ReviewCreateDTO dto,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reviewService.createOrUpdateReview(userId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ReviewCreateDTO dto,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.ok(reviewService.update(id, userId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        reviewService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ReviewDTO> like(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        reviewLikeService.like(id, userId);

        return ResponseEntity.ok(reviewService.getById(id));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<ReviewDTO> unlike(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        reviewLikeService.unlike(id, userId);

        return ResponseEntity.ok(reviewService.getById(id));
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<Map<String, Long>> getLikes(@PathVariable Integer id) {
        return ResponseEntity.ok(
                Map.of(
                        "reviewId", Long.valueOf(id),
                        "likes", reviewLikeService.count(id)
                )
        );
    }
}