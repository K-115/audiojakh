package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Artist;
import com.makersacademy.audiojakh.model.Track;
import com.makersacademy.audiojakh.repository.AlbumRepository;
import com.makersacademy.audiojakh.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class ArtistsController {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/artists/{id}")
    public String show(@PathVariable Long id, Model model) {
        Artist artist = artistRepository.findById(id).orElseThrow();
        model.addAttribute("artist", artist);
        model.addAttribute("albums", albumRepository.findAlbumsByArtistId(id));

        return "artists/show";
    }
}
