package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.DTOs.DTOProfileJoin;
import com.makersacademy.audiojakh.model.*;
import com.makersacademy.audiojakh.repository.*;
import com.makersacademy.audiojakh.service.SpotifyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FavouriteAlbumRepository favouriteAlbumRepository;
    @Autowired
    private FavouriteArtistRepository favouriteArtistRepository;
    @Autowired
    private FavouriteTracksRepository favouriteTracksRepository;
    @Autowired
    private FollowRepository followRepository;
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

    @GetMapping("/profile/{username}")
    public String showUserProfile(@PathVariable String username, Model model, HttpSession session) {
        User me = currentUser();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        model.addAttribute("currentUser", me);

        boolean isOwnProfile = me != null && me.getId().equals(user.getId());
        boolean isFollowingThisUser = false;

        if (me != null && !isOwnProfile) {
            isFollowingThisUser = followRepository.isFollowing(me.getId(), user.getId()) > 0;
        }

        List<Review> reviews = reviewRepository.findByUserIdOrderByDateOfReviewDesc(user.getId());
        long followers = followRepository.countFollowersByUserId(user.getId());
        long following = followRepository.countFollowingByUserId(user.getId());
        long reviewCount = reviewRepository.countReviewsByUserId(user.getId());

        Long userIdKey = (long) user.getId().intValue();
        List<String> favouriteAlbumIds = favouriteAlbumRepository.findAlbumIdsByUserId(userIdKey);
        List<String> favouriteTrackIds = favouriteTracksRepository.findTrackIdsByUserId(userIdKey);
        List<String> favouriteArtistIds = favouriteArtistRepository.findArtistIdsByUserId(userIdKey);

        List<se.michaelthelin.spotify.model_objects.specification.Album> favAlbums = favouriteAlbumIds.stream()
                .limit(1)
                .map(id -> spotifyService.getAlbum(id))
                .toList();

        List<se.michaelthelin.spotify.model_objects.specification.Track> favSongs = favouriteTrackIds.stream()
                .limit(1)
                .map(id -> spotifyService.getTrack(id))
                .toList();

        List<se.michaelthelin.spotify.model_objects.specification.Artist> favArtists = favouriteArtistIds.stream()
                .limit(1)
                .map(id -> spotifyService.getArtist(id))
                .toList();

        DTOProfileJoin profile = new DTOProfileJoin(
                user,
                favAlbums,
                favSongs,
                favArtists,
                reviews,
                isOwnProfile,
                isFollowingThisUser,
                followers,
                following,
                reviewCount
        );

        model.addAttribute("profile", profile);
        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("isFollowing", isFollowingThisUser);

        if (me != null) {
            session.setAttribute("profilePicture", me.getProfilePicture());
            session.setAttribute("userId", me.getId());
            session.setAttribute("userUsername", me.getUsername());
        }

        return "profile";
    }
}

