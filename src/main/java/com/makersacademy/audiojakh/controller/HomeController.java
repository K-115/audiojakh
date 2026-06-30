package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Review;
import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.ReviewRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import com.makersacademy.audiojakh.service.SpotifyService;
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
	private ReviewRepository reviewRepository;

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


	@GetMapping("/")
	public String index(Model model) {
		User me = currentUser();
		model.addAttribute("currentUser", me);

			List<se.michaelthelin.spotify.model_objects.specification.Track> trendingSongs = spotifyService.getTrendingSongsThisWeek();
			List<se.michaelthelin.spotify.model_objects.specification.AlbumSimplified> trendingAlbums = spotifyService.getTrendingAlbums();

			model.addAttribute("trendingSongs", trendingSongs);
			model.addAttribute("trendingAlbums", trendingAlbums);

			List<Review> trendingReviews = reviewRepository.findAllByOrderByLikesDesc();
			if (trendingReviews.size() > 6) {
				trendingReviews = trendingReviews.subList(0, 6);
			}
			model.addAttribute("trendingReviews", trendingReviews);
		return "home";
	}
}
