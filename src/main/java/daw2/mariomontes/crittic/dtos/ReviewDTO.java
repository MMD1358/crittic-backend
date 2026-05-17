package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO {
    private Integer reviewId;
    private Integer videogameId;
    private String videogameTitle;

    private Long userId;
    private String username;
    private String userImage;

    private BigDecimal rating;
    private String content;
    private LocalDateTime createdAt;

    private Long likesCount;
    private Long answersCount;
    private Boolean likedByCurrentUser;
}