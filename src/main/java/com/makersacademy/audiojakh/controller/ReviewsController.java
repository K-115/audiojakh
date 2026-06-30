package com.makersacademy.audiojakh.controller;
import com.makersacademy.audiojakh.model.*;
import com.makersacademy.audiojakh.repository.*;
import com.makersacademy.audiojakh.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ReviewsController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentLikeRepository commentLikeRepository;

    private User currentUser() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) {
			return null;
		}
		String email = (String) oidc.getAttributes().get("email");
		return userRepository.findUserByEmailAddress(email).orElse(null);
	}


@GetMapping("/reviews")
public String index(@RequestParam(value = "sort", required = false, defaultValue = "recent") String sort,
                    Model model) {
    User me = currentUser();
    model.addAttribute("currentUser", me);
    model.addAttribute("currentSort", sort);

    List<Review> reviews;
    if ("liked".equalsIgnoreCase(sort)) {
        reviews = reviewRepository.findAllByOrderByLikesDesc();
    } else {
        reviews = reviewRepository.findAllByOrderByIdDesc();
    }

    List<ReviewView> reviewViews = new ArrayList<>();
    for (Review r : reviews) {
        long count = reviewLikeRepository.countByReviewId(r.getId());
        boolean liked = me != null && reviewLikeRepository.existsByReviewIdAndUserId(r.getId(), me.getId());

        List<Comment> rawComments = commentRepository.findByReviewId(r.getId());
        List<CommentView> commentViews = new ArrayList<>();

        for (Comment c : rawComments) {
            long commentLikes = commentLikeRepository.countByCommentId(c.getId());
            boolean commentLikedByMe = me != null && commentLikeRepository.existsByCommentIdAndUserId(c.getId(), me.getId());

            commentViews.add(new CommentView(c, commentLikes, commentLikedByMe));
        }

        reviewViews.add(new ReviewView(r, count, liked, commentViews));
    }

    model.addAttribute("reviewViews", reviewViews);

    return "posts/reviews_page";
}

    @GetMapping("/reviews/new")
    public String showCreateForm(@RequestParam(value = "query", required = false) String query, Model model) {
        User me = currentUser();
        if (me == null) {
            return "redirect:/login";
        }

        if (!model.containsAttribute("review")) {
            model.addAttribute("review", new Review());
        }

        if (query != null && !query.trim().isEmpty()) {
            List<Track> searchTracks = spotifyService.searchTracks(query);
            model.addAttribute("searchTracks", searchTracks);
            model.addAttribute("query", query);
        } else {
            model.addAttribute("searchTracks", List.of());
        }

        return "posts/new_review";
    }

    @PostMapping("/reviews")
    public String create(@RequestParam(value = "trackId", required = false) String trackId,
                         @RequestParam(value = "albumId", required = false) String albumId,
                         @ModelAttribute Review review,
                         RedirectAttributes redirectAttributes) {
        User currentUser = currentUser();
        User me = currentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (trackId != null && !trackId.trim().isEmpty()) {
            review.setTrackSpotifyId(trackId);
            try {
                se.michaelthelin.spotify.model_objects.specification.Track spTrack = spotifyService.getTrack(trackId);
                review.setTrackName(spTrack.getName());

                if (spTrack.getArtists() != null && spTrack.getArtists().length > 0) {
                    review.setArtistName(spTrack.getArtists()[0].getName());
                }

                if (spTrack.getAlbum() != null && spTrack.getAlbum().getImages() != null && spTrack.getAlbum().getImages().length > 0) {
                    review.setImageUrl(spTrack.getAlbum().getImages()[0].getUrl());
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch track image/meta from Spotify: " + e.getMessage());
            }
        }



        if (trackId != null && !trackId.trim().isEmpty()) {
            if (reviewRepository.existsByUserIdAndTrackSpotifyId(currentUser.getId(), trackId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You have already reviewed this song!");
                return "redirect:/reviews/new";
            }
            review.setTrackSpotifyId(trackId);
            Track spTrack = spotifyService.getTrack(trackId);
            review.setTrackName(spTrack.getName());

        }
        else if (albumId != null && !albumId.trim().isEmpty()) {
            if (reviewRepository.existsByUserIdAndAlbumSpotifyId(currentUser.getId(), albumId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You have already reviewed this album!");
                return "redirect:/reviews/new";
            }
            review.setAlbumSpotifyId(albumId);
                Album spAlbum = spotifyService.getAlbum(albumId);
                review.setAlbumName(spAlbum.getName());
        }

        review.setUser(currentUser);
        review.setLikes(0);
        reviewRepository.save(review);

        return "redirect:/reviews";
    }

    @PostMapping("/reviews/{id}/like")
    public String like(@PathVariable Long id) {
        User me = currentUser();
        if (me != null) {
            reviewLikeRepository.findByReviewIdAndUserId(id, me.getId()).ifPresentOrElse(
                    reviewLikeRepository::delete,
                    () -> reviewLikeRepository.save(new ReviewLike(me.getId(), id)));

            reviewRepository.findById(id).ifPresent(r -> {
                r.setLikes((int) reviewLikeRepository.countByReviewId(id));
                reviewRepository.save(r);
            });
        }
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User me = currentUser();
        if (me == null) {
            return "redirect:/login";
        }

        var reviewOpt = reviewRepository.findById(id);
        if (reviewOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Review not found.");
            return "redirect:/reviews";
        }

        Review review = reviewOpt.get();
        if (review.getUser() == null || !review.getUser().getId().equals(me.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to edit this review.");
            return "redirect:/reviews";
        }

        model.addAttribute("review", review);
        return "posts/edit_review_page";
    }

    @PostMapping("/reviews/{id}/edit")
    public RedirectView updateReview(@PathVariable Long id,
                                     @RequestParam("content") String content,
                                     @RequestParam("rating") Integer rating,
                                     RedirectAttributes redirectAttributes) {
        User me = currentUser();
        if (me == null) {
            return new RedirectView("/login");
        }

        reviewRepository.findById(id).ifPresentOrElse(review -> {
            if (review.getUser() != null && review.getUser().getId().equals(me.getId())) {
                review.setContent(content);
                review.setRating(rating);
                reviewRepository.save(review);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized action.");
            }
        }, () -> redirectAttributes.addFlashAttribute("errorMessage", "Review not found."));

        return new RedirectView("/reviews");
    }

}
