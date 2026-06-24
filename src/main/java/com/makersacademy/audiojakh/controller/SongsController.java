package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Track;
import com.makersacademy.audiojakh.repository.AlbumRepository;
import com.makersacademy.audiojakh.repository.ArtistRepository;
import com.makersacademy.audiojakh.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SongsController {

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/songs/{spotifyId}")
    public String show(@PathVariable String spotifyId, Model model) {
        Track track = trackRepository.findById(spotifyId).orElseThrow();
        model.addAttribute("track", track);

        if (track.getAlbumId() != null) {
            albumRepository.findById(track.getAlbumId())
                    .ifPresent(album -> model.addAttribute("album", album));
        }

        if (track.getArtistId() != null) {
            artistRepository.findById(track.getArtistId())
                    .ifPresent(artist -> model.addAttribute("artist", artist));
        }
        return "songs/show";
    }
}
