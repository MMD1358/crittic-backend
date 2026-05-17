package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.VideogameCreateDTO;
import daw2.mariomontes.crittic.dtos.VideogameDTO;
import daw2.mariomontes.crittic.entities.*;
import daw2.mariomontes.crittic.repositories.ReviewLikeRepository;
import daw2.mariomontes.crittic.repositories.ReviewRepository;
import daw2.mariomontes.crittic.repositories.VideogameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import daw2.mariomontes.crittic.repositories.VideogameLikeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class VideogameService {

    private final VideogameRepository videogameRepository;
    private final AuthorService authorService;
    private final ReviewRepository reviewRepository;
    private final VideogameLikeRepository videogameLikeRepository;
    private final UserService userService;

    public VideogameService(VideogameRepository videogameRepository,
                            AuthorService authorService,
                            ReviewRepository reviewRepository,
                            ReviewLikeRepository reviewLikeRepository,
                            VideogameLikeRepository videogameLikeRepository,
                            UserService userService) {
        this.videogameRepository = videogameRepository;
        this.authorService = authorService;
        this.reviewRepository = reviewRepository;
        this.videogameLikeRepository = videogameLikeRepository;
        this.userService = userService;
    }

    public Page<VideogameDTO> getAll(String search, Pageable pageable) {
        Page<Videogame> page = (search == null || search.isBlank())
                ? videogameRepository.findAll(pageable)
                : videogameRepository.findByTitleStartingWithIgnoreCase(search, pageable);

        return page.map(this::toDTO);
    }

    public VideogameDTO getById(Integer id) {
        return toDTO(findEntity(id));
    }

    public VideogameDTO create(VideogameCreateDTO dto) {
        Videogame videogame = new Videogame();
        updateFields(videogame, dto);
        return toDTO(videogameRepository.save(videogame));
    }

    public VideogameDTO update(Integer id, VideogameCreateDTO dto) {
        Videogame videogame = findEntity(id);
        updateFields(videogame, dto);
        return toDTO(videogameRepository.save(videogame));
    }

    public void delete(Integer id) {
        videogameRepository.delete(findEntity(id));
    }

    public Videogame findEntity(Integer id) {
        return videogameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("El videojuego no existe."));
    }

    private void updateFields(Videogame videogame, VideogameCreateDTO dto) {
        videogame.setTitle(dto.getTitle());
        videogame.setReleaseDate(dto.getReleaseDate());
        videogame.setGenre(dto.getGenre());
        videogame.setAuthor(authorService.findEntity(dto.getAuthorId()));
        videogame.setImage(dto.getImage());
        videogame.setDescription(dto.getDescription());
    }

    private VideogameDTO toDTO(Videogame videogame) {
        VideogameDTO dto = new VideogameDTO();
        dto.setVideogameId(videogame.getVideogameId());
        dto.setTitle(videogame.getTitle());
        dto.setReleaseDate(videogame.getReleaseDate());
        dto.setGenre(videogame.getGenre());
        dto.setAuthorId(videogame.getAuthor().getAuthorId());
        dto.setAuthorName(videogame.getAuthor().getName());
        var reviews = reviewRepository.findByVideogame_VideogameId(videogame.getVideogameId(), Pageable.unpaged()).getContent();
        dto.setReviewCount((long) reviews.size());
        dto.setAverageRating(reviewRepository.getAverageRatingByVideogameId(videogame.getVideogameId()));
        dto.setImage(videogame.getImage());
        dto.setDescription(videogame.getDescription());
        dto.setLikesCount(videogameLikeRepository.countByVideogame_VideogameId(videogame.getVideogameId()));
        dto.setLikedByCurrentUser(isLikedByCurrentUser(videogame.getVideogameId()));
        dto.setCurrentUserRating(getCurrentUserRating(videogame.getVideogameId()));
        return dto;
    }

    public VideogameDTO like(Integer videogameId) {
        User user = getCurrentUser();

        VideogameLikeId id = new VideogameLikeId(user.getUserId(), videogameId);

        if (!videogameLikeRepository.existsById(id)) {
            VideogameLike like = new VideogameLike();
            like.setId(id);
            like.setUser(user);
            like.setVideogame(findEntity(videogameId));
            videogameLikeRepository.save(like);
        }

        return getById(videogameId);
    }

    public VideogameDTO unlike(Integer videogameId) {
        User user = getCurrentUser();

        VideogameLikeId id = new VideogameLikeId(user.getUserId(), videogameId);
        videogameLikeRepository.deleteById(id);

        return getById(videogameId);
    }

    private Boolean isLikedByCurrentUser(Integer videogameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();

        if (username == null || username.equals("anonymousUser")) {
            return false;
        }

        Long userId = userService.getIdByUsername(username);

        return videogameLikeRepository.existsById(new VideogameLikeId(userId, videogameId));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            throw new IllegalArgumentException("Debes iniciar sesión para dar like.");
        }

        return userService.findEntityByUsername(authentication.getName());
    }

    public VideogameDTO rateVideogame(Integer videogameId, Integer rating) {
        User user = getCurrentUser();
        Videogame videogame = findEntity(videogameId);

        Review review = reviewRepository
                .findByUser_UserIdAndVideogame_VideogameId(user.getUserId(), videogameId)
                .orElse(new Review());

        review.setUser(user);
        review.setVideogame(videogame);
        review.setRating(BigDecimal.valueOf(rating));

        if (review.getContent() == null) {
            review.setContent("");
        }

        reviewRepository.save(review);

        return getById(videogameId);
    }

    private BigDecimal getCurrentUserRating(Integer videogameId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();

        if (username == null || username.equals("anonymousUser")) {
            return null;
        }

        Long userId = userService.getIdByUsername(username);

        return reviewRepository
                .findByUser_UserIdAndVideogame_VideogameId(userId, videogameId)
                .map(Review::getRating)
                .orElse(null);
    }
}
