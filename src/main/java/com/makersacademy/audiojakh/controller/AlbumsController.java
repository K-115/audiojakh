package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Album;
import com.makersacademy.audiojakh.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlbumsController {

    @Autowired
    AlbumRepository albumRepository;

    @GetMapping(/"albums")
    public String index(Model model) {
        Iterable<Album> albums = albumRepository.findAll();
        model.addAttribute("albums", albums);
        return "albums/index";
    }
}
