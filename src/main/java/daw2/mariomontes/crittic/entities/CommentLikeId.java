package daw2.mariomontes.crittic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentLikeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "comment_id")
    private Integer commentId;
}