package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Track;
import com.makersacademy.audiojakh.service.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private SpotifyService spotifyService;

    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query != null && !query.trim().isEmpty()) {
            List<Track> searchResults = spotifyService.searchTracks(query);
            model.addAttribute("results", searchResults);
            model.addAttribute("query", query);
        }
        return "search";
    }
}
