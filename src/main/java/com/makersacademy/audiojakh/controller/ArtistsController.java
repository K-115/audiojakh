package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import com.makersacademy.audiojakh.service.SpotifyService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.Arrays;
import java.util.List;

@Controller
public class ArtistsController {

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


    @GetMapping("/artists/{id}")
    public String showArtistProfile(@PathVariable("id") String artistId, Model model) {
        Artist artist = spotifyService.getArtist(artistId);
        List<AlbumSimplified> albums = spotifyService.getArtistDiscography(artistId);
        User me = currentUser();
        model.addAttribute("currentUser", me);
        model.addAttribute("artist", artist);
        model.addAttribute("albums", albums);
        return "artists/show";
    }
}
