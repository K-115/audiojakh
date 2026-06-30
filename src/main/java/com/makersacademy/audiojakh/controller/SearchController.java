//package com.makersacademy.audiojakh.controller;
//
//import com.makersacademy.audiojakh.model.User;
//import com.makersacademy.audiojakh.repository.FollowRepository;
//import com.makersacademy.audiojakh.repository.UserRepository;
//import com.makersacademy.audiojakh.service.SpotifyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import se.michaelthelin.spotify.model_objects.specification.Track;
//
//import java.util.List;
//
//@Controller
//public class SearchController {
//
//    @Autowired
//    private SpotifyService spotifyService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private FollowRepository followRepository;
//
//    private User currentUser() {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
//        String email = (String) oidc.getAttributes().get("email");
//        return userRepository.findUserByEmailAddress(email).orElse(null);
//    }
//
//
//    @GetMapping("/search")
//    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
//        if (query != null && !query.trim().isEmpty()) {
//            List<Track> searchResults = spotifyService.searchTracks(query);
//            model.addAttribute("results", searchResults);
//            model.addAttribute("query", query);
//        }
//        return "search";
//    }
//}

package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import com.makersacademy.audiojakh.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private UserRepository userRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        Object principal = auth.getPrincipal();
        String email = null;

        if (principal instanceof DefaultOidcUser oidc) {
            email = (String) oidc.getAttributes().get("email");
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof String strPrincipal) {
            email = strPrincipal;
        }

        if (email == null) return null;
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @GetMapping("/search")
    public String globalSearch(@RequestParam(value = "query", required = false) String query, Model model) {
        User me = currentUser();
        model.addAttribute("currentUser", me);

        if (me != null) {
            model.addAttribute("currentUsername", me.getUsername());
        }

        if (query != null && !query.trim().isEmpty()) {
            String cleanQuery = query.trim();
            model.addAttribute("query", cleanQuery);

            List<User> userResults = userRepository.searchUsers(cleanQuery);
            model.addAttribute("userResults", userResults);

            try {
                List<Track> trackResults = spotifyService.searchTracks(cleanQuery);
                List<AlbumSimplified> albumResults = spotifyService.searchAlbums(cleanQuery);
                List<Artist> artistResults = spotifyService.searchArtists(cleanQuery);

                model.addAttribute("trackResults", trackResults);
                model.addAttribute("albumResults", albumResults);
                model.addAttribute("artistResults", artistResults);
            } catch (Exception e) {
                System.err.println("Spotify Global Search Error: " + e.getMessage());
                model.addAttribute("trackResults", List.of());
                model.addAttribute("albumResults", List.of());
                model.addAttribute("artistResults", List.of());
            }
        }

        return "search_results";
    }
}


