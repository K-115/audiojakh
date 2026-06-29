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
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.util.List;

@Controller
public class AlbumsController {

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

    @GetMapping("/albums/{id}")
    public String show(@PathVariable("id") String albumId, Model model) {
            Album album = spotifyService.getAlbum(albumId);
            List<TrackSimplified> tracks = spotifyService.getAlbumTracks(albumId);
        User me = currentUser();
        model.addAttribute("currentUser", me);
            model.addAttribute("album", album);
            model.addAttribute("tracks", tracks);
            return "albums/show";
    }
}
