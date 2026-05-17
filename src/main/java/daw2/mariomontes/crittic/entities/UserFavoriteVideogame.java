package daw2.mariomontes.crittic.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_favorite_videogame")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteVideogame {

    @EmbeddedId
    private UserFavoriteVideogameId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videogameId")
    @JoinColumn(name = "videogame_id")
    private Videogame videogame;

    @CreationTimestamp
    private LocalDateTime createdAt;
}