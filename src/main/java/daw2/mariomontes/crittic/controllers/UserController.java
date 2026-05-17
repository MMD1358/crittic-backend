package daw2.mariomontes.crittic.controllers;

import daw2.mariomontes.crittic.dtos.UserDTO;
import daw2.mariomontes.crittic.services.UserFollowService;
import daw2.mariomontes.crittic.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserFollowService userFollowService;

    public UserController(UserService userService, UserFollowService userFollowService) {
        this.userService = userService;
        this.userFollowService = userFollowService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserById(userService.getIdByUsername(authentication.getName())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long id) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("userId", id);
        body.put("followers", userFollowService.countFollowers(id));
        body.put("following", userFollowService.countFollowing(id));
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> follow(@PathVariable Long id, Principal principal) {
        userFollowService.follow(userService.getIdByUsername(principal.getName()), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/follow")
    public ResponseEntity<Void> unfollow(@PathVariable Long id, Principal principal) {
        userFollowService.unfollow(userService.getIdByUsername(principal.getName()), id);
        return ResponseEntity.noContent().build();
    }
}
