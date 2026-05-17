package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private Integer commentId;
    private Integer reviewId;
    private Long userId;
    private String username;
    private String userImage;
    private String content;
    private LocalDateTime createdAt;
    private Long likesCount;
    private Boolean likedByCurrentUser;
}
