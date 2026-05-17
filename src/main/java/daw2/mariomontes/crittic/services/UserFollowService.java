package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.entities.UserFollow;
import daw2.mariomontes.crittic.entities.UserFollowId;
import daw2.mariomontes.crittic.repositories.UserFollowRepository;
import org.springframework.stereotype.Service;

@Service
public class UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserService userService;

    public UserFollowService(UserFollowRepository userFollowRepository, UserService userService) {
        this.userFollowRepository = userFollowRepository;
        this.userService = userService;
    }

    public long countFollowers(Long userId) {
        return userFollowRepository.countByFollowed_UserId(userId);
    }

    public long countFollowing(Long userId) {
        return userFollowRepository.countByFollower_UserId(userId);
    }

    public void follow(Long currentUserId, Long targetUserId) {
        if (currentUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("No puedes seguirte a ti mismo.");
        }
        UserFollowId id = new UserFollowId(currentUserId, targetUserId);
        if (userFollowRepository.existsById(id)) {
            return;
        }
        UserFollow follow = new UserFollow();
        follow.setId(id);
        follow.setFollower(userService.findEntityById(currentUserId));
        follow.setFollowed(userService.findEntityById(targetUserId));
        userFollowRepository.save(follow);
    }

    public void unfollow(Long currentUserId, Long targetUserId) {
        userFollowRepository.deleteById(new UserFollowId(currentUserId, targetUserId));
    }
}
