package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.DTOs.DTOProfileJoin;
import com.makersacademy.audiojakh.model.*;
import com.makersacademy.audiojakh.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private ReviewRepository reviewRepository;
    @Autowired
    private FavouriteAlbumRepository favouriteAlbumRepository;
    @Autowired
    private FavouriteArtistRepository favouriteArtistRepository;
    @Autowired
    private FavouriteTracksRepository favouriteTracksRepository;

    private User currentUser(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @GetMapping("/profile/{username}")
    public String showUserProfile(@PathVariable String username, Model model, HttpSession session) {
        User me = currentUser();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

//        List<Review> reviews = reviewRepository.findByReviewerOrderByDateOfReviewDesc(user.getId());
        List<Album> favAlbums = favouriteAlbumRepository.findFavouriteAlbumsByUserId(user.getId());
        List<Song> favSongs = favouriteTracksRepository.findFavouriteTracksByUserId(user.getId());
        List<Artist> favArtists = favouriteArtistRepository.findFavouriteArtistsByUserId(user.getId());

        boolean isOwnProfile = me != null && me.getId().equals(user.getId());

        DTOProfileJoin profile = new DTOProfileJoin(user, favAlbums, favSongs, favArtists, isOwnProfile);

        model.addAttribute("profile", profile);

        Optional<User> savedUser = userRepository.findUserByEmailAddress(user.getEmailAddress());

        if (savedUser.isPresent()) {
            session.setAttribute("profilePicture", savedUser.get().getProfilePicture());
            session.setAttribute("userId", savedUser.get().getId());
            session.setAttribute("userUsername", savedUser.get().getUsername());
        }

        return "profile";
    }
}
