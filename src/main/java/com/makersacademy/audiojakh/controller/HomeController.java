//package com.makersacademy.audiojakh.controller;
//
//import com.makersacademy.audiojakh.model.*;
//import com.makersacademy.audiojakh.repository.*;
//import com.makersacademy.audiojakh.service.SpotifyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//
//@Controller
//public class HomeController {
//
//	@Autowired
//	private UserRepository userRepository;
//
//
//	@Autowired
//	private ReviewRepository reviewRepository;
//
//	private User currentUser() {
//		var auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) {
//			return null;
//		}
//		String email = (String) oidc.getAttributes().get("email");
//		return userRepository.findUserByEmailAddress(email).orElse(null);
//	}
//
////	@GetMapping("/")
////	public String showHomepage(Model model) {
////		User me = currentUser();
////		model.addAttribute("currentUser", me);
////
////		List<Track> trendingSongs = tracksRepository.findTop5ByOrderBySpotifyIdDesc();
////		List<Artist> trendingArtists = artistRepository.findTop5ByOrderByArtistNameAsc();
////		List<Album> trendingAlbums = albumRepository.findTop5ByOrderByReleaseDateDesc();
////		List<Review> trendingReviews = reviewRepository.findTop5TrendingReviewsThisWeek();
////
////		model.addAttribute("trendingSongs", trendingSongs);
////		model.addAttribute("trendingArtists", trendingArtists);
////		model.addAttribute("trendingAlbums", trendingAlbums);
////		model.addAttribute("trendingReviews", trendingReviews);
////
////		return "home";
////	}
//@Autowired
//private SpotifyService spotifyService;
//
//	@GetMapping("/")
//	public String index(Model model) {
//		// Example: Pass a default trending search query so the home page tracks loop isn't empty
//		try {
//			List<se.michaelthelin.spotify.model_objects.specification.Track> trendingTracks =
//					spotifyService.searchTracks("Top Hits");
//			model.addAttribute("tracks", trendingTracks);
//		} catch (Exception e) {
//			// Fallback to an empty list instead of letting the app break
//			model.addAttribute("tracks", List.of());
//		}
//
//		return "home";
//	}
//}

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
