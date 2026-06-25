package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.*;
import com.makersacademy.audiojakh.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TrackRepository tracksRepository;

	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private AlbumRepository albumRepository;

	// @Autowired
	// private ReviewRepository reviewRepository;

	private User currentUser() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) {
			return null;
		}
		String email = (String) oidc.getAttributes().get("email");
		return userRepository.findUserByEmailAddress(email).orElse(null);
	}

	@GetMapping("/")
	public String showHomepage(Model model) {
		// 1. Pass down user details if someone is logged in (useful for navbars)
		User me = currentUser();
		model.addAttribute("currentUser", me);

		// 2. Fetch the trending data streams
		// Replace 'findTop5...' with whatever logic/naming your system uses to define trending
		List<Track> trendingSongs = tracksRepository.findTop5ByOrderBySpotifyIdDesc();
		List<Artist> trendingArtists = artistRepository.findTop5ByOrderByNameAsc();
		List<Album> trendingAlbums = albumRepository.findTop5ByOrderByReleaseDateDesc();
		// List<Review> trendingReviews = reviewRepository.findTop5ByOrderByDateOfReviewDesc();

		// 3. Bind lists to the UI Model context
		model.addAttribute("trendingSongs", trendingSongs);
		model.addAttribute("trendingArtists", trendingArtists);
		model.addAttribute("trendingAlbums", trendingAlbums);
		// model.addAttribute("trendingReviews", trendingReviews);

		// Returns your homepage template file: src/main/resources/templates/home.html
		return "home";
	}
}
