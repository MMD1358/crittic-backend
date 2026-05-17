package daw2.mariomontes.crittic.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class VideogameDTO {
    private Integer videogameId;
    private String title;
    private LocalDate releaseDate;
    private String genre;
    private String image;
    private Integer authorId;
    private String authorName;
    private Double averageRating;
    private Long reviewCount;
    private String description;
    private Long likesCount;
    private Boolean likedByCurrentUser;
    private BigDecimal currentUserRating;
}
