package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.*;
import daw2.mariomontes.crittic.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getProfile(username));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDTO> updateMe(@RequestBody UserProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateMe(dto));
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<UserProfileDTO> follow(@PathVariable String username) {
        return ResponseEntity.ok(profileService.follow(username));
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<UserProfileDTO> unfollow(@PathVariable String username) {
        return ResponseEntity.ok(profileService.unfollow(username));
    }

    @GetMapping("/{username}/favorites")
    public ResponseEntity<List<VideogameDTO>> getFavorites(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getFavorites(username));
    }

    @PostMapping("/me/favorites/{videogameId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Integer videogameId) {
        profileService.addFavorite(videogameId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/favorites/{videogameId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Integer videogameId) {
        profileService.removeFavorite(videogameId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getReviews(username));
    }

    @GetMapping("/{username}/comments")
    public ResponseEntity<List<ProfileCommentDTO>> getComments(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getProfileComments(username));
    }

    @PostMapping("/{username}/comments")
    public ResponseEntity<ProfileCommentDTO> createComment(
            @PathVariable String username,
            @Valid @RequestBody ProfileCommentCreateDTO dto
    ) {
        return ResponseEntity.ok(profileService.createProfileComment(username, dto));
    }
}