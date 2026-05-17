package daw2.mariomontes.crittic.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "videogame_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideogameLike {

    @EmbeddedId
    private VideogameLikeId id;

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