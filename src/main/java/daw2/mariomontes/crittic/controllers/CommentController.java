package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.CommentCreateDTO;
import daw2.mariomontes.crittic.dtos.CommentDTO;
import daw2.mariomontes.crittic.services.CommentService;
import daw2.mariomontes.crittic.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<CommentDTO>> getByReviewParam(
            @RequestParam Integer reviewId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(commentService.getByReview(reviewId, pageable));
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<Page<CommentDTO>> getByReviewPath(
            @PathVariable Integer reviewId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(commentService.getByReview(reviewId, pageable));
    }

    @PostMapping
    public ResponseEntity<CommentDTO> create(
            @Valid @RequestBody CommentCreateDTO dto,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.create(userId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        commentService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<CommentDTO> like(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.ok(commentService.like(id, userId));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<CommentDTO> unlike(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.ok(commentService.unlike(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody CommentCreateDTO dto,
            Authentication authentication
    ) {
        Long userId = userService.getIdByUsername(authentication.getName());
        return ResponseEntity.ok(commentService.update(id, userId, dto));
    }
}