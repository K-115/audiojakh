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

	@Autowired
	private ReviewRepository reviewRepository;

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
		User me = currentUser();
		model.addAttribute("currentUser", me);

		List<Track> trendingSongs = tracksRepository.findTop5ByOrderBySpotifyIdDesc();
		List<Artist> trendingArtists = artistRepository.findTop5ByOrderByNameAsc();
		List<Album> trendingAlbums = albumRepository.findTop5ByOrderByReleaseDateDesc();
		List<Review> trendingReviews = reviewRepository.findTop5TrendingReviewsThisWeek();

		model.addAttribute("trendingSongs", trendingSongs);
		model.addAttribute("trendingArtists", trendingArtists);
		model.addAttribute("trendingAlbums", trendingAlbums);
		model.addAttribute("trendingReviews", trendingReviews);

		return "home";
	}
}
