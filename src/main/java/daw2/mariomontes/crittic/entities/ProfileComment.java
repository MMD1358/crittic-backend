package daw2.mariomontes.crittic.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_comment_id")
    private Integer profileCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_user_id", nullable = false)
    private User profileUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", nullable = false)
    private User authorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private ProfileComment parentComment;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}