//package com.makersacademy.audiojakh.controller;
//
//import com.makersacademy.audiojakh.model.Track;
//import com.makersacademy.audiojakh.repository.AlbumRepository;
//import com.makersacademy.audiojakh.repository.ArtistRepository;
//import com.makersacademy.audiojakh.repository.TrackRepository;
//import com.makersacademy.audiojakh.service.SpotifyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@Controller
//public class SongsController {
//
//    @Autowired
//    TrackRepository trackRepository;
//
//    @Autowired
//    AlbumRepository albumRepository;
//
//    @Autowired
//    ArtistRepository artistRepository;
//
//    @Autowired
//    SpotifyService spotifyService;
//
//    @GetMapping("/songs")
//    public String index(Model model) {
//        Iterable<Track> tracks = trackRepository.findAll();
//        model.addAttribute("tracks", tracks);
//        return "songs/index";
//    }
//
//    @GetMapping("/songs/{spotifyId}")
//    public String show(@PathVariable String spotifyId, Model model) {
//        Track track = spotifyService.getOrCacheTrack(spotifyId);
//        model.addAttribute("track", track);
//
//        if (track.getAlbumId() != null) {
//            albumRepository.findById(track.getAlbumId())
//                    .ifPresent(album -> model.addAttribute("album", album));
//        }
//
//        if (track.getArtistId() != null) {
//            artistRepository.findById(String.valueOf(track.getArtistId()))
//                    .ifPresent(artist -> model.addAttribute("artist", artist));
//        }
//        return "songs/show";
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@Controller
public class SongsController {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private UserRepository userRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

//    @GetMapping("/songs")
//    public String index(@RequestParam(value = "query", required = false) String query, Model model) {
//        if (query != null && !query.trim().isEmpty()) {
//            List<se.michaelthelin.spotify.model_objects.specification.Track> tracks = spotifyService.searchTracks(query);
//            model.addAttribute("tracks", tracks);
//            model.addAttribute("query", query);
//        }
//        return "songs/index";
//    }


    @GetMapping("/songs")
    public String index(@RequestParam(value = "query", required = false) String query,
                        @RequestParam(value = "year", required = false) String year,
                        @RequestParam(value = "genre", required = false) String genre,
                        @RequestParam(value = "orderBy", required = false) String orderBy,
                        Model model) {

        User me = currentUser();
        model.addAttribute("currentUser", me);
        model.addAttribute("query", query);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedOrderBy", orderBy);
        model.addAttribute("popularSongs", spotifyService.getMostPopularSongs());
        model.addAttribute("hotReleases", spotifyService.getNewReleases());

        boolean isFiltering = (query != null && !query.trim().isEmpty())
                || (year != null && !year.trim().isEmpty())
                || (genre != null && !genre.trim().isEmpty());

        if (isFiltering) {
            List<Track> filteredTracks = spotifyService.searchTracksAdvanced(query, year, genre, orderBy);
            model.addAttribute("tracks", filteredTracks);
        } else {
            model.addAttribute("tracks", List.of());
        }

        return "songs/index";
    }


    @GetMapping("/songs/{spotifyId}")
    public String show(@PathVariable String spotifyId, Model model) {
            se.michaelthelin.spotify.model_objects.specification.Track track = spotifyService.getTrack(spotifyId);
            model.addAttribute("track", track);
        User me = currentUser();
        model.addAttribute("currentUser", me);

        return "songs/show";
        }
    }

