package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.ReviewCreateDTO;
import daw2.mariomontes.crittic.dtos.ReviewDTO;
import daw2.mariomontes.crittic.entities.Review;
import daw2.mariomontes.crittic.entities.User;
import daw2.mariomontes.crittic.repositories.ReviewLikeRepository;
import daw2.mariomontes.crittic.repositories.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import daw2.mariomontes.crittic.repositories.CommentRepository;
import daw2.mariomontes.crittic.entities.ReviewLikeId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final VideogameService videogameService;
    private final UserService userService;
    private final ReviewLikeRepository reviewLikeRepository;
    private final CommentRepository commentRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            VideogameService videogameService,
            UserService userService,
            ReviewLikeRepository reviewLikeRepository,
            CommentRepository commentRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.videogameService = videogameService;
        this.userService = userService;
        this.reviewLikeRepository = reviewLikeRepository;
        this.commentRepository = commentRepository;
    }

    public Page<ReviewDTO> getAll(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<ReviewDTO> getByVideogame(Integer videogameId, Pageable pageable) {
        return reviewRepository.findByVideogame_VideogameId(videogameId, pageable).map(this::toDTO);
    }

    public Page<ReviewDTO> getByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUser_UserId(userId, pageable).map(this::toDTO);
    }

    public ReviewDTO getById(Integer id) {
        return toDTO(findEntity(id));
    }

    public ReviewDTO create(Long userId, ReviewCreateDTO dto) {
        User user = userService.findEntityById(userId);
        var videogame = videogameService.findEntity(dto.getVideogameId());
        reviewRepository.findByUserAndVideogame(user, videogame).ifPresent(review -> {
            throw new IllegalArgumentException("Ya existe una review de este usuario para ese videojuego.");
        });
        Review review = new Review();
        review.setUser(user);
        review.setVideogame(videogame);
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        return toDTO(reviewRepository.save(review));
    }

    public ReviewDTO update(Integer id, Long userId, ReviewCreateDTO dto) {
        Review review = findEntity(id);
        if (!review.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("No puedes modificar esta review.");
        }
        review.setVideogame(videogameService.findEntity(dto.getVideogameId()));
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        return toDTO(reviewRepository.save(review));
    }

    public void delete(Integer id, Long userId) {
        Review review = findEntity(id);
        if (!review.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("No puedes eliminar esta review.");
        }
        reviewRepository.delete(review);
    }

    public Review findEntity(Integer id) {
        return reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("La review no existe."));
    }

    private ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();

        dto.setReviewId(review.getReviewId());
        dto.setVideogameId(review.getVideogame().getVideogameId());
        dto.setVideogameTitle(review.getVideogame().getTitle());

        dto.setUserId(review.getUser().getUserId());
        dto.setUsername(review.getUser().getUsername());
        dto.setUserImage(review.getUser().getImage());

        dto.setRating(review.getRating());
        dto.setContent(review.getContent());
        dto.setCreatedAt(review.getCreatedAt());

        dto.setLikesCount(reviewLikeRepository.countByReview_ReviewId(review.getReviewId()));
        dto.setLikedByCurrentUser(isLikedByCurrentUser(review.getReviewId()));

        dto.setAnswersCount(commentRepository.countByReviewReviewId(review.getReviewId()));

        return dto;
    }

    public List<ReviewDTO> getReviewsByVideogame(Integer videogameId) {
        return reviewRepository
                .findByVideogame_VideogameId(videogameId, Pageable.unpaged())
                .getContent()
                .stream()
                .filter(review -> review.getContent() != null && !review.getContent().isBlank())
                .map(this::toDTO)
                .toList();
    }

    public ReviewDTO createOrUpdateReview(Long userId, ReviewCreateDTO dto) {
        User user = userService.findEntityById(userId);
        var videogame = videogameService.findEntity(dto.getVideogameId());

        Review review = reviewRepository
                .findByUserAndVideogame(user, videogame)
                .orElse(new Review());

        review.setUser(user);
        review.setVideogame(videogame);
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());

        return toDTO(reviewRepository.save(review));
    }

    private Boolean isLikedByCurrentUser(Integer reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();

        if (username == null || username.equals("anonymousUser")) {
            return false;
        }

        Long userId = userService.getIdByUsername(username);

        return reviewLikeRepository.existsById(new ReviewLikeId(userId, reviewId));
    }

    public List<ReviewDTO> getReviewsByUser(Long userId) {
        return reviewRepository
                .findByUser_UserId(userId, Pageable.unpaged())
                .getContent()
                .stream()
                .filter(review -> review.getContent() != null && !review.getContent().isBlank())
                .map(this::toDTO)
                .toList();
    }

}
