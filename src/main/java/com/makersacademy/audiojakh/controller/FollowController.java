package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.FollowRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @PostMapping("/profile/{username}/follow")
    public String followUser(@PathVariable String username) {
        User me = currentUser();
        User targetUser = userRepository.findUserByUsername(username).orElseThrow();

        if (me != null && !me.getId().equals(targetUser.getId())) {
            followRepository.follow(me.getId(), targetUser.getId());
        }
        return "redirect:/profile/" + username;
    }

    @PostMapping("/profile/{username}/unfollow")
    public String unfollowUser(@PathVariable String username) {
        User me = currentUser();
        User targetUser = userRepository.findUserByUsername(username).orElseThrow();

        if (me != null && !me.getId().equals(targetUser.getId())) {
            followRepository.unfollow(me.getId(), targetUser.getId());
        }
        return "redirect:/profile/" + username;
    }
}
