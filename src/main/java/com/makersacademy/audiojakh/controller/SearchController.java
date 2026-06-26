package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.FollowRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
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
    @Autowired
    private FollowRepository followRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @GetMapping("/search")
    public String searchResults(
            @RequestParam String query,
            Model model,
            @AuthenticationPrincipal OAuth2User principal) {

        List<User> results = userRepository.searchUsers(query);
        User me = currentUser();


        model.addAttribute("searchResults", results);
        model.addAttribute("searchTerm", query);
        model.addAttribute("currentUserEntity", me);

//        if(principal != null) {
//            String email = principal.getAttribute("email");
//            Optional<User> currentUser = userRepository.findUserByEmailAddress(email);
//            currentUser.ifPresent(user -> model.addAttribute("currentUsername", user.getUsername()));
//        }
        if (me != null) {
            model.addAttribute("currentUsername", me.getUsername());
            List<Long> followedUserIds = results.stream()
                    .filter(user -> followRepository.isFollowing(me.getId(), user.getId()) > 0)
                    .map(User::getId)
                    .toList();

            model.addAttribute("followedUserIds", followedUserIds);
        }

        return "search_results";
    }
}
