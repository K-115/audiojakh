////package com.makersacademy.audiojakh.controller;
////
////import com.makersacademy.audiojakh.model.ReviewLike;
////import com.makersacademy.audiojakh.model.ReviewView;
////import com.makersacademy.audiojakh.model.User;
////import com.makersacademy.audiojakh.repository.*;
////import com.makersacademy.audiojakh.model.Review;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.core.annotation.AuthenticationPrincipal;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
////import org.springframework.security.oauth2.core.user.OAuth2User;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.servlet.mvc.support.RedirectAttributes;
////import org.springframework.web.servlet.view.RedirectView;
////
////import java.util.ArrayList;
////import java.util.List;
////
////@Controller
////public class ReviewsController {
////
////    @Autowired
////    ReviewRepository reviewRepository;
////
////    @Autowired
////    UserRepository userRepository;
////
////    @Autowired
////    TrackRepository trackRepository;
////
////    @Autowired
////    AlbumRepository albumRepository;
////
////    @Autowired
////    ReviewLikeRepository reviewLikeRepository;
////
////    private User currentUser() {
////        var auth = SecurityContextHolder.getContext().getAuthentication();
////        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) {
////            return null;
////        }
////        String email = (String) oidc.getAttributes().get("email");
////        return userRepository.findUserByEmailAddress(email).orElse(null);
////    }
////
////    @GetMapping("/reviews")
////    public String index(@RequestParam(value = "albumId", required = false) String albumId, Model model, @ModelAttribute("errorMessage") String errorMessage) {
////        User me = currentUser();
////        model.addAttribute("currentUser", me);
////        Iterable<Review> reviews = reviewRepository.findAll();
////        model.addAttribute("reviews", reviews);
////
////        if (!model.containsAttribute("review")) {
////            model.addAttribute("review", new Review());
////        }
////
////        model.addAttribute("allAlbums", albumRepository.findAll());
////
////        List<ReviewView> reviewViews = new ArrayList<>();
////        for (Review r : reviewRepository.findAll()) {
////            long count = reviewLikeRepository.countByReviewId(r.getId());
////            boolean liked = me != null && reviewLikeRepository.existsByReviewIdAndUserId(r.getId(), me.getId());
////            reviewViews.add(new ReviewView(r, count, liked));
////        }
////        model.addAttribute("reviewViews", reviewViews);
////
////        if (albumId != null && !albumId.trim().isEmpty()) {
////            model.addAttribute("allTracks", trackRepository.findTracksByAlbumId(albumId));
////            model.addAttribute("selectedAlbumId", albumId);
////        } else {
////            model.addAttribute("allTracks", List.of());
////            model.addAttribute("selectedAlbumId", null);
////        }
////
////        return "posts/reviews_page";
////    }
////
////    @PostMapping("/reviews")
////    public RedirectView create(@RequestParam(value = "trackId", required = false) String trackId,
////                               @RequestParam(value = "albumId", required = false) String albumId,
////                               @ModelAttribute Review review,
////                               @AuthenticationPrincipal OAuth2User principal,
////                               RedirectAttributes redirectAttributes) {
////        User currentUser = currentUser();
////        if (principal == null) {
////            return new RedirectView("/login");
////        }
////// String emailAddress = null;
//////         if (principal instanceof DefaultOidcUser) {
//////             emailAddress = ((DefaultOidcUser) principal).getEmail();
//////         } else {
//////             emailAddress = principal.getAttribute("email");
//////         }
////
//////         if (emailAddress == null) {
//////             emailAddress = principal.getAttribute("preferred_username");
//////         }
////
//////         User currentUser = userRepository.findUserByEmailAddress(emailAddress)
//////                 .orElseThrow(() -> new IllegalStateException("Authenticated user record not found in the database."));
//////        String username = principal.getAttribute("email");
//////        if (username == null) {
//////            username = principal.getAttribute("preferred_username");
//////        }
////
//////        Long currentUserId = userRepository.findUserByUsername(username)
//////                .map(User::getId)
//////                .orElse(1L);
////
////        if (trackId != null && !trackId.trim().isEmpty()) {
////            if (reviewRepository.existsByUserIdAndTrackSpotifyId(currentUser.getId(), trackId)) {
////                redirectAttributes.addFlashAttribute("errorMessage", "You have already reviewed this song!");
////                return new RedirectView("/reviews?albumId=" + albumId);
////            }
////            trackRepository.findById(trackId).ifPresent(review::setTrack);
////        } else if (albumId != null && !albumId.trim().isEmpty()) {
////            if (reviewRepository.existsByUserIdAndAlbumSpotifyId(currentUser.getId(), albumId)) {
////                redirectAttributes.addFlashAttribute("errorMessage", "You have already reviewed this album!");
////                return new RedirectView("/reviews?albumId=" + albumId);
////            }
////            albumRepository.findById(albumId).ifPresent(review::setAlbum);
////        }
////
////        review.setUser(currentUser);
////        review.setLikes(0);
////        reviewRepository.save(review);
////
////        return new RedirectView("/reviews");
////    }
////
////    @PostMapping("/reviews/{id}/like")
////    public RedirectView like(@PathVariable Long id) {
////        User me = currentUser();
////        if (me != null) {
////            reviewLikeRepository.findByReviewIdAndUserId(id, me.getId()).ifPresentOrElse(
////                    reviewLikeRepository::delete,                                  // already liked -> unlike
////                    () -> reviewLikeRepository.save(new ReviewLike(me.getId(), id))); // not yet -> like
////            // keep reviews.likes in sync (home "Trending Reviews", profile, and the ORDER BY likes query read it)
////            reviewRepository.findById(id).ifPresent(r -> {
////                r.setLikes((int) reviewLikeRepository.countByReviewId(id));
////                reviewRepository.save(r);
////            });
////        }
////        return new RedirectView("/reviews");
////    }
////}


package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.ReviewLike;
import com.makersacademy.audiojakh.model.ReviewView;
import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.*;
import com.makersacademy.audiojakh.model.Review;
import com.makersacademy.audiojakh.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
            reviewViews.add(new ReviewView(r, count, liked));
        }

        model.addAttribute("reviewViews", reviewViews);
        model.addAttribute("currentSort", sort);

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
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (trackId != null && !trackId.trim().isEmpty()) {
            review.setTrackSpotifyId(trackId);

                se.michaelthelin.spotify.model_objects.specification.Track spTrack = spotifyService.getTrack(trackId);
                review.setTrackName(spTrack.getName());

                if (spTrack.getArtists() != null && spTrack.getArtists().length > 0) {
                    review.setArtistName(spTrack.getArtists()[0].getName());
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
}
