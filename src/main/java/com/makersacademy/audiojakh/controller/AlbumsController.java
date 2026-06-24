package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Album;
import com.makersacademy.audiojakh.model.Track;
import com.makersacademy.audiojakh.repository.AlbumRepository;
import com.makersacademy.audiojakh.repository.ArtistRepository;
import com.makersacademy.audiojakh.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AlbumsController {

    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    TrackRepository trackRepository;
    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/albums")
    public String index(Model model) {
        Iterable<Album> albums = albumRepository.findAll();
        model.addAttribute("albums", albums);
        return "albums/index";
    }

    @GetMapping("/albums/{spotifyId")
    public String show(@PathVariable String spotifyId, Model model) {
        Album album = albumRepository.findById(spotifyId).orElseThrow();
        List<Track> tracks = trackRepository.findTracksByAlbumId(spotifyId);
        model.addAttribute("album", album);
        model.addAttribute("tracks", tracks);
    }
}
