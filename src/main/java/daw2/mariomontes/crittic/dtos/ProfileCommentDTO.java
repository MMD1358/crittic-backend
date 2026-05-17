package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProfileCommentDTO {

    private Integer profileCommentId;

    private Long profileUserId;
    private String profileUsername;

    private Long authorUserId;
    private String authorUsername;
    private String authorImage;

    private Integer parentCommentId;

    private String content;
    private LocalDateTime createdAt;

    private Long likesCount;
    private Boolean likedByCurrentUser;
    private Boolean ownComment;

    private List<ProfileCommentDTO> replies;
}