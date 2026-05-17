package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private Long userId;
    private String username;
    private String image;
    private String description;
    private Long followersCount;
    private Long followingCount;
    private Boolean followedByCurrentUser;
    private Boolean ownProfile;
}