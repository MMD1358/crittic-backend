package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.entities.ReviewLike;
import daw2.mariomontes.crittic.entities.ReviewLikeId;
import daw2.mariomontes.crittic.repositories.ReviewLikeRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewLikeService(ReviewLikeRepository reviewLikeRepository, ReviewService reviewService, UserService userService) {
        this.reviewLikeRepository = reviewLikeRepository;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    public long count(Integer reviewId) {
        return reviewLikeRepository.countByReview_ReviewId(reviewId);
    }

    public void like(Integer reviewId, Long userId) {
        ReviewLikeId id = new ReviewLikeId(userId, reviewId);
        if (reviewLikeRepository.existsById(id)) {
            return;
        }
        ReviewLike like = new ReviewLike();
        like.setId(id);
        like.setUser(userService.findEntityById(userId));
        like.setReview(reviewService.findEntity(reviewId));
        reviewLikeRepository.save(like);
    }

    public void unlike(Integer reviewId, Long userId) {
        reviewLikeRepository.deleteById(new ReviewLikeId(userId, reviewId));
    }
}
