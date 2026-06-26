package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.TrackRepository;
import com.makersacademy.audiojakh.repository.AlbumRepository;
import com.makersacademy.audiojakh.model.Review;
import com.makersacademy.audiojakh.repository.ReviewRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class ReviewsController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    AlbumRepository albumRepository;

    @GetMapping("/reviews")
    public String index(@RequestParam(value = "albumId", required = false) String albumId,
                        Model model,
                        @ModelAttribute("errorMessage") String errorMessage) {
        Iterable<Review> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);

        if (!model.containsAttribute("review")) {
            model.addAttribute("review", new Review());
        }

        model.addAttribute("allAlbums", albumRepository.findAll());

        if (albumId != null && !albumId.trim().isEmpty()) {
            model.addAttribute("allTracks", trackRepository.findTracksByAlbumId(albumId));
            model.addAttribute("selectedAlbumId", albumId);
        } else {
            model.addAttribute("allTracks", List.of());
            model.addAttribute("selectedAlbumId", null);
        }

        return "posts/reviews_page";
    }

    @PostMapping("/reviews")
    public RedirectView create(@RequestParam(value = "trackId", required = false) String trackId,
                               @RequestParam(value = "albumId", required = false) String albumId,
                               @ModelAttribute Review review,
                               @AuthenticationPrincipal OAuth2User principal,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return new RedirectView("/login");
        }

        String emailAddress = null;
        if (principal instanceof DefaultOidcUser) {
            emailAddress = ((DefaultOidcUser) principal).getEmail();
        } else {
            emailAddress = principal.getAttribute("email");
        }

        if (emailAddress == null) {
            emailAddress = principal.getAttribute("preferred_username");
        }

        User currentUser = userRepository.findUserByEmailAddress(emailAddress)
                .orElseThrow(() -> new IllegalStateException("Authenticated user record not found in the database."));

        if (trackId != null && !trackId.trim().isEmpty()) {
            if (reviewRepository.existsByUserIdAndTrackSpotifyId(currentUser.getId(), trackId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You have already reviewed this song!");
                return new RedirectView("/reviews?albumId=" + albumId);
            }
            trackRepository.findById(trackId).ifPresent(review::setTrack);
        } else if (albumId != null && !albumId.trim().isEmpty()) {
            if (reviewRepository.existsByUserIdAndAlbumSpotifyId(currentUser.getId(), albumId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You have already reviewed this album!");
                return new RedirectView("/reviews?albumId=" + albumId);
            }
            albumRepository.findById(albumId).ifPresent(review::setAlbum);
        }

        review.setUser(currentUser);
        review.setLikes(0);
        reviewRepository.save(review);

        return new RedirectView("/reviews");
    }
}
