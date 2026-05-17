package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.ProfileCommentCreateDTO;
import daw2.mariomontes.crittic.dtos.ProfileCommentDTO;
import daw2.mariomontes.crittic.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile-comments")
public class ProfileCommentController {

    private final ProfileService profileService;

    public ProfileCommentController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<ProfileCommentDTO> reply(
            @PathVariable Integer commentId,
            @Valid @RequestBody ProfileCommentCreateDTO dto
    ) {
        return ResponseEntity.ok(profileService.replyToComment(commentId, dto));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ProfileCommentDTO> update(
            @PathVariable Integer commentId,
            @Valid @RequestBody ProfileCommentCreateDTO dto
    ) {
        return ResponseEntity.ok(profileService.updateComment(commentId, dto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Integer commentId) {
        profileService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<ProfileCommentDTO> like(@PathVariable Integer commentId) {
        return ResponseEntity.ok(profileService.likeComment(commentId));
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<ProfileCommentDTO> unlike(@PathVariable Integer commentId) {
        return ResponseEntity.ok(profileService.unlikeComment(commentId));
    }
}