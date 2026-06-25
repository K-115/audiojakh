package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public String searchResults(@RequestParam String query, Model model) {
        List<User> results = userRepository.searchUsers(query);

        model.addAttribute("searchResults", results);
        model.addAttribute("searchTerm", query);

        return "members/search_results";
    }
}
