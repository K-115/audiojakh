package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.*;
import com.makersacademy.audiojakh.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FavouritesController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavouriteTracksRepository favouriteTracksRepository;
    @Autowired
    private FavouriteAlbumRepository favouriteAlbumRepository;
    @Autowired
    private FavouriteArtistRepository favouriteArtistRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) return null;
        Object principal = auth.getPrincipal();
        String email = null;
        if (principal instanceof DefaultOidcUser oidc) {
            email = (String) oidc.getAttributes().get("email");
        }
        if (email == null) return null;
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @PostMapping("/favourites/track")
    public String setFavouriteTrack(@RequestParam("id") String trackId) {
        User me = currentUser();
        if (me == null) return "redirect:/login";

        Long userId = (long) me.getId().intValue();
        favouriteTracksRepository.deleteByUserIdNative(userId);

        FavouriteTrack ft = new FavouriteTrack();
        ft.setUser(me);
        ft.setTrackId(trackId);
        favouriteTracksRepository.save(ft);

        return "redirect:/profile/" + me.getUsername();
    }

    @PostMapping("/favourites/album")
    public String setFavouriteAlbum(@RequestParam("id") String albumId) {
        User me = currentUser();
        if (me == null) return "redirect:/login";

        Long userId = (long) me.getId().intValue();
        favouriteAlbumRepository.deleteByUserIdNative(userId);

        FavouriteAlbum fa = new FavouriteAlbum();
        fa.setUser(me);
        fa.setAlbumId(albumId);
        favouriteAlbumRepository.save(fa);

        return "redirect:/profile/" + me.getUsername();
    }

    @PostMapping("/favourites/artist")
    public String setFavouriteArtist(@RequestParam("id") String artistId) {
        User me = currentUser();
        if (me == null) return "redirect:/login";

        Long userId = (long) me.getId().intValue();
        favouriteArtistRepository.deleteByUserIdNative(userId);

        FavouriteArtist far = new FavouriteArtist();
        far.setUser(me);
        far.setArtistId(artistId);
        favouriteArtistRepository.save(far);

        return "redirect:/profile/" + me.getUsername();
    }
}
