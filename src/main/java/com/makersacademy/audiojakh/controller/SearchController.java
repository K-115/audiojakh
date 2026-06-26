package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public String searchResults(
            @RequestParam String query,
            Model model,
            @AuthenticationPrincipal OAuth2User principal) {

        List<User> results = userRepository.searchUsers(query);

        model.addAttribute("searchResults", results);
        model.addAttribute("searchTerm", query);

        if(principal != null) {
            String email = principal.getAttribute("email");
            Optional<User> currentUser = userRepository.findUserByEmailAddress(email);
            currentUser.ifPresent(user -> model.addAttribute("currentUsername", user.getUsername()));
        }
        return "search_results";
    }
}
