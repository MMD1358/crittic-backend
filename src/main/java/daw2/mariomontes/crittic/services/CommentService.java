package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.CommentCreateDTO;
import daw2.mariomontes.crittic.dtos.CommentDTO;
import daw2.mariomontes.crittic.entities.Comment;
import daw2.mariomontes.crittic.entities.CommentLike;
import daw2.mariomontes.crittic.entities.CommentLikeId;
import daw2.mariomontes.crittic.entities.User;
import daw2.mariomontes.crittic.repositories.CommentLikeRepository;
import daw2.mariomontes.crittic.repositories.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReviewService reviewService;
    private final UserService userService;

    public CommentService(
            CommentRepository commentRepository,
            CommentLikeRepository commentLikeRepository,
            ReviewService reviewService,
            UserService userService
    ) {
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    public Page<CommentDTO> getByReview(Integer reviewId, Pageable pageable) {
        return commentRepository
                .findByReviewReviewId(reviewId, pageable)
                .map(this::toDTO);
    }

    public CommentDTO create(Long userId, CommentCreateDTO dto) {
        Comment comment = new Comment();
        comment.setReview(reviewService.findEntity(dto.getReviewId()));
        comment.setUser(userService.findEntityById(userId));
        comment.setContent(dto.getContent());

        return toDTO(commentRepository.save(comment));
    }

    public void delete(Integer id, Long userId) {
        Comment comment = findEntity(id);

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("No puedes eliminar este comentario.");
        }

        commentRepository.delete(comment);
    }

    public CommentDTO like(Integer commentId, Long userId) {
        User user = userService.findEntityById(userId);
        Comment comment = findEntity(commentId);

        CommentLikeId id = new CommentLikeId(userId, commentId);

        if (!commentLikeRepository.existsById(id)) {
            CommentLike like = new CommentLike();
            like.setId(id);
            like.setUser(user);
            like.setComment(comment);

            commentLikeRepository.save(like);
        }

        return toDTO(comment);
    }

    public CommentDTO unlike(Integer commentId, Long userId) {
        CommentLikeId id = new CommentLikeId(userId, commentId);

        if (commentLikeRepository.existsById(id)) {
            commentLikeRepository.deleteById(id);
        }

        return toDTO(findEntity(commentId));
    }

    public Comment findEntity(Integer id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El comentario no existe."));
    }

    private CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();

        dto.setCommentId(comment.getCommentId());
        dto.setReviewId(comment.getReview().getReviewId());

        dto.setUserId(comment.getUser().getUserId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setUserImage(comment.getUser().getImage());

        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        dto.setLikesCount(commentLikeRepository.countByCommentCommentId(comment.getCommentId()));
        dto.setLikedByCurrentUser(isLikedByCurrentUser(comment.getCommentId()));

        return dto;
    }

    private Boolean isLikedByCurrentUser(Integer commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();

        if (username == null || username.equals("anonymousUser")) {
            return false;
        }

        Long userId = userService.getIdByUsername(username);

        return commentLikeRepository.existsById(new CommentLikeId(userId, commentId));
    }

    public CommentDTO update(Integer id, Long userId, CommentCreateDTO dto) {
        Comment comment = findEntity(id);

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("You cannot edit this comment.");
        }

        comment.setContent(dto.getContent());

        return toDTO(commentRepository.save(comment));
    }

}