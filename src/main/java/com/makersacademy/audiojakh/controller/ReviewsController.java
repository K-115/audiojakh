package com.makersacademy.audiojakh.controller;

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
import org.springframework.web.servlet.view.RedirectView;

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
    public String index(Model model) {
        Iterable<Review> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);
        model.addAttribute("review", new Review());

        model.addAttribute("allTracks", trackRepository.findAll());
        model.addAttribute("allAlbums", albumRepository.findAll());
        return "posts/reviews_page";
    }

    @PostMapping("/reviews")
    public RedirectView create(@RequestParam(value = "trackId", required = false) String trackId,
                               @RequestParam(value = "albumId", required = false) String albumId,
                               @ModelAttribute Review review,
                               @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return new RedirectView("/login");
        }

        if (trackId != null && !trackId.trim().isEmpty()) {
            trackRepository.findById(trackId).ifPresent(review::setTrack);
        }

        if (albumId != null && !albumId.trim().isEmpty()) {
            albumRepository.findById(albumId).ifPresent(review::setAlbum);
        }

        String username = principal.getAttribute("email");
        if (username == null) {
            username = principal.getAttribute("preferred_username");
        }

        userRepository.findUserByUsername(username)
                .ifPresentOrElse(
                        user -> review.setUserId(user.getId()),
                        () -> review.setUserId(1L)
                );

        review.setLikes(0);
        reviewRepository.save(review);

        return new RedirectView("/reviews");
    }

}



