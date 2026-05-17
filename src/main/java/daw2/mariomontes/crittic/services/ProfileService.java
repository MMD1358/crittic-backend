package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.*;
import daw2.mariomontes.crittic.entities.*;
import daw2.mariomontes.crittic.repositories.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final UserService userService;
    private final UserFollowRepository userFollowRepository;
    private final UserFavoriteVideogameRepository favoriteRepository;
    private final VideogameService videogameService;
    private final ReviewService reviewService;
    private final ProfileCommentRepository profileCommentRepository;
    private final ProfileCommentLikeRepository profileCommentLikeRepository;

    public ProfileService(
            UserService userService,
            UserFollowRepository userFollowRepository,
            UserFavoriteVideogameRepository favoriteRepository,
            VideogameService videogameService,
            ReviewService reviewService,
            ProfileCommentRepository profileCommentRepository,
            ProfileCommentLikeRepository profileCommentLikeRepository
    ) {
        this.userService = userService;
        this.userFollowRepository = userFollowRepository;
        this.favoriteRepository = favoriteRepository;
        this.videogameService = videogameService;
        this.reviewService = reviewService;
        this.profileCommentRepository = profileCommentRepository;
        this.profileCommentLikeRepository = profileCommentLikeRepository;
    }

    public UserProfileDTO getProfile(String username) {
        User profileUser = userService.findEntityByUsername(username);
        User currentUser = getCurrentUserOrNull();

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(profileUser.getUserId());
        dto.setUsername(profileUser.getUsername());
        dto.setImage(profileUser.getImage());
        dto.setDescription(profileUser.getDescription());

        dto.setFollowersCount(userFollowRepository.countByFollowed_UserId(profileUser.getUserId()));
        dto.setFollowingCount(userFollowRepository.countByFollower_UserId(profileUser.getUserId()));

        if (currentUser == null) {
            dto.setFollowedByCurrentUser(false);
            dto.setOwnProfile(false);
        } else {
            dto.setFollowedByCurrentUser(
                    userFollowRepository.existsById(
                            new UserFollowId(currentUser.getUserId(), profileUser.getUserId())
                    )
            );
            dto.setOwnProfile(currentUser.getUserId().equals(profileUser.getUserId()));
        }

        return dto;
    }

    public UserProfileDTO updateMe(UserProfileUpdateDTO dto) {
        User currentUser = getCurrentUser();

        currentUser.setDescription(dto.getDescription());

        if (dto.getImage() != null) {
            currentUser.setImage(dto.getImage());
        }

        userService.save(currentUser);

        return getProfile(currentUser.getUsername());
    }

    public UserProfileDTO follow(String username) {
        User currentUser = getCurrentUser();
        User profileUser = userService.findEntityByUsername(username);

        if (currentUser.getUserId().equals(profileUser.getUserId())) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }

        UserFollowId id = new UserFollowId(currentUser.getUserId(), profileUser.getUserId());

        if (!userFollowRepository.existsById(id)) {
            UserFollow follow = new UserFollow();
            follow.setId(id);
            follow.setFollower(currentUser);
            follow.setFollowed(profileUser);
            userFollowRepository.save(follow);
        }

        return getProfile(username);
    }

    public UserProfileDTO unfollow(String username) {
        User currentUser = getCurrentUser();
        User profileUser = userService.findEntityByUsername(username);

        UserFollowId id = new UserFollowId(currentUser.getUserId(), profileUser.getUserId());
        userFollowRepository.deleteById(id);

        return getProfile(username);
    }

    public List<VideogameDTO> getFavorites(String username) {
        return favoriteRepository.findByUser_Username(username)
                .stream()
                .map(favorite -> videogameService.getById(favorite.getVideogame().getVideogameId()))
                .toList();
    }

    public void addFavorite(Integer videogameId) {
        User currentUser = getCurrentUser();
        Videogame videogame = videogameService.findEntity(videogameId);

        UserFavoriteVideogameId id = new UserFavoriteVideogameId(currentUser.getUserId(), videogameId);

        if (!favoriteRepository.existsById(id)) {
            UserFavoriteVideogame favorite = new UserFavoriteVideogame();
            favorite.setId(id);
            favorite.setUser(currentUser);
            favorite.setVideogame(videogame);
            favoriteRepository.save(favorite);
        }
    }

    public void removeFavorite(Integer videogameId) {
        User currentUser = getCurrentUser();

        favoriteRepository.deleteById(
                new UserFavoriteVideogameId(currentUser.getUserId(), videogameId)
        );
    }

    public List<ReviewDTO> getReviews(String username) {
        User user = userService.findEntityByUsername(username);

        return reviewService.getReviewsByUser(user.getUserId());
    }

    public List<ProfileCommentDTO> getProfileComments(String username) {
        return profileCommentRepository
                .findByProfileUser_UsernameAndParentCommentIsNullOrderByCreatedAtDesc(username)
                .stream()
                .map(this::toProfileCommentDTO)
                .toList();
    }

    public ProfileCommentDTO createProfileComment(String username, ProfileCommentCreateDTO dto) {
        User currentUser = getCurrentUser();
        User profileUser = userService.findEntityByUsername(username);

        ProfileComment comment = new ProfileComment();
        comment.setProfileUser(profileUser);
        comment.setAuthorUser(currentUser);
        comment.setContent(dto.getContent());

        return toProfileCommentDTO(profileCommentRepository.save(comment));
    }

    public ProfileCommentDTO replyToComment(Integer commentId, ProfileCommentCreateDTO dto) {
        User currentUser = getCurrentUser();
        ProfileComment parent = findProfileComment(commentId);

        ProfileComment reply = new ProfileComment();
        reply.setProfileUser(parent.getProfileUser());
        reply.setAuthorUser(currentUser);
        reply.setParentComment(parent);
        reply.setContent(dto.getContent());

        return toProfileCommentDTO(profileCommentRepository.save(reply));
    }

    public ProfileCommentDTO updateComment(Integer commentId, ProfileCommentCreateDTO dto) {
        User currentUser = getCurrentUser();
        ProfileComment comment = findProfileComment(commentId);

        if (!comment.getAuthorUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("You cannot edit this comment.");
        }

        comment.setContent(dto.getContent());

        return toProfileCommentDTO(profileCommentRepository.save(comment));
    }

    public void deleteComment(Integer commentId) {
        User currentUser = getCurrentUser();
        ProfileComment comment = findProfileComment(commentId);

        if (!comment.getAuthorUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("You cannot delete this comment.");
        }

        profileCommentRepository.delete(comment);
    }

    public ProfileCommentDTO likeComment(Integer commentId) {
        User currentUser = getCurrentUser();
        ProfileComment comment = findProfileComment(commentId);

        ProfileCommentLikeId id = new ProfileCommentLikeId(currentUser.getUserId(), commentId);

        if (!profileCommentLikeRepository.existsById(id)) {
            ProfileCommentLike like = new ProfileCommentLike();
            like.setId(id);
            like.setUser(currentUser);
            like.setProfileComment(comment);
            profileCommentLikeRepository.save(like);
        }

        return toProfileCommentDTO(comment);
    }

    public ProfileCommentDTO unlikeComment(Integer commentId) {
        User currentUser = getCurrentUser();

        profileCommentLikeRepository.deleteById(
                new ProfileCommentLikeId(currentUser.getUserId(), commentId)
        );

        return toProfileCommentDTO(findProfileComment(commentId));
    }

    private ProfileComment findProfileComment(Integer commentId) {
        return profileCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Profile comment not found."));
    }

    private ProfileCommentDTO toProfileCommentDTO(ProfileComment comment) {
        User currentUser = getCurrentUserOrNull();

        ProfileCommentDTO dto = new ProfileCommentDTO();

        dto.setProfileCommentId(comment.getProfileCommentId());

        dto.setProfileUserId(comment.getProfileUser().getUserId());
        dto.setProfileUsername(comment.getProfileUser().getUsername());

        dto.setAuthorUserId(comment.getAuthorUser().getUserId());
        dto.setAuthorUsername(comment.getAuthorUser().getUsername());
        dto.setAuthorImage(comment.getAuthorUser().getImage());

        dto.setParentCommentId(
                comment.getParentComment() == null
                        ? null
                        : comment.getParentComment().getProfileCommentId()
        );

        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        dto.setLikesCount(
                profileCommentLikeRepository.countByProfileComment_ProfileCommentId(comment.getProfileCommentId())
        );

        if (currentUser == null) {
            dto.setLikedByCurrentUser(false);
            dto.setOwnComment(false);
        } else {
            dto.setLikedByCurrentUser(
                    profileCommentLikeRepository.existsById(
                            new ProfileCommentLikeId(currentUser.getUserId(), comment.getProfileCommentId())
                    )
            );
            dto.setOwnComment(
                    currentUser.getUserId().equals(comment.getAuthorUser().getUserId())
            );
        }

        dto.setReplies(
                profileCommentRepository
                        .findByParentComment_ProfileCommentIdOrderByCreatedAtAsc(comment.getProfileCommentId())
                        .stream()
                        .map(this::toProfileCommentDTO)
                        .toList()
        );

        return dto;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            throw new IllegalArgumentException("You must log in.");
        }

        return userService.findEntityByUsername(authentication.getName());
    }

    private User getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            return null;
        }

        return userService.findEntityByUsername(authentication.getName());
    }
}