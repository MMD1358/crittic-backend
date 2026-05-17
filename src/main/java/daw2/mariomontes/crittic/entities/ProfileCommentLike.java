package daw2.mariomontes.crittic.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile_comment_like")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCommentLike {

    @EmbeddedId
    private ProfileCommentLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("profileCommentId")
    @JoinColumn(name = "profile_comment_id")
    private ProfileComment profileComment;

    @CreationTimestamp
    private LocalDateTime createdAt;
}