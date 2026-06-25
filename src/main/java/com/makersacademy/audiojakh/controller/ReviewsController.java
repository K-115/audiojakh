package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Track;
import com.makersacademy.audiojakh.model.Album;
import com.makersacademy.audiojakh.repository.TrackRepository;
import com.makersacademy.audiojakh.repository.AlbumRepository;
import com.makersacademy.audiojakh.model.Review;
import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.ReviewRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

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

    // Handles loading the page (GET /reviews)
    @GetMapping("/reviews")
    public String index(Model model) {
        Iterable<Review> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);
        model.addAttribute("review", new Review());
        // Pass tracks and albums to the frontend dropdowns
        model.addAttribute("allTracks", trackRepository.findAll());
        model.addAttribute("allAlbums", albumRepository.findAll());
        return "posts/reviews_page";
    }

    @PostMapping("/reviews")
    public RedirectView create(@ModelAttribute Review review, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return new RedirectView("/login");
        }

        // 1. Safe extraction tailored for Okta OpenID Connect attributes
        String username = principal.getAttribute("preferred_username");

        if (username == null) {
            username = principal.getAttribute("email"); // If your app treats the email as the username
        }
        if (username == null) {
            username = principal.getAttribute("sub"); // Okta unique numeric/alphanumeric identifier
        }

        // 2. Prevent the 500 database crash if no string was resolved
        if (username == null) {
            throw new IllegalStateException("Could not extract an identification attribute from Okta. Profile attributes: " + principal.getAttributes());
        }

        // 3. Find the user inside your local users table
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isPresent()) {
            User currentUser = userOptional.get();
            review.setUserId(currentUser.getId());
            review.setLikes(0);
            reviewRepository.save(review);
        } else {
            // This fails gracefully with an explicit message if your user sync step was skipped
            throw new RuntimeException("Authenticated Okta user '" + username + "' is missing from the local database users table.");
        }

        return new RedirectView("/reviews");
    }
}



