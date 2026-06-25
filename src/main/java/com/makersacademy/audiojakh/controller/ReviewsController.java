package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.TrackRepository;
import com.makersacademy.audiojakh.repository.AlbumRepository;
import com.makersacademy.audiojakh.model.Review;
import com.makersacademy.audiojakh.repository.ReviewRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String index(@RequestParam(value = "albumId", required = false) String albumId, Model model) {
        Iterable<Review> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);
        model.addAttribute("review", new Review());
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

        String username = principal.getAttribute("email");
        if (username == null) {
            username = principal.getAttribute("preferred_username");
        }

        Long currentUserId = userRepository.findUserByUsername(username)
                .map(User::getId)
                .orElse(1L);

        if (trackId != null && !trackId.trim().isEmpty()) {
            if (reviewRepository.existsByUserIdAndTrackSpotifyId(currentUserId, trackId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You have already reviewed this song!");
                return new RedirectView("/reviews?albumId=" + albumId);
            }
            trackRepository.findById(trackId).ifPresent(review::setTrack);
        } else if (albumId != null && !albumId.trim().isEmpty()) {
            if (reviewRepository.existsByUserIdAndAlbumSpotifyId(currentUserId, albumId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You have already reviewed this album!");
                return new RedirectView("/reviews?albumId=" + albumId);
            }
            albumRepository.findById(albumId).ifPresent(review::setAlbum);
        }

        review.setUserId(currentUserId);
        review.setLikes(0);
        reviewRepository.save(review);

        return new RedirectView("/reviews");
    }
}



