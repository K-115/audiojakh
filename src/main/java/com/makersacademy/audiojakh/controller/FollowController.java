package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.FollowRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import com.makersacademy.audiojakh.service.CurrentUserService;
import com.makersacademy.audiojakh.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/profile/{username}/follow")
    public String followUser(@PathVariable String username, HttpServletRequest request) {
        User me = currentUserService.get();
        User targetUser = userRepository.findUserByUsername(username).orElseThrow();

        if (me != null && !me.getId().equals(targetUser.getId())) {
            followRepository.follow(me.getId(), targetUser.getId());
            notificationService.createFollowNotification(targetUser, me);
        }
        String referer = request.getHeader("Referer");
        return referer != null ? "redirect:" + referer : "redirect:/profile/" + username;
//        return "redirect:/profile/" + username;
    }

    @PostMapping("/profile/{username}/unfollow")
    public String unfollowUser(@PathVariable String username, HttpServletRequest request) {
        User me = currentUserService.get();
        User targetUser = userRepository.findUserByUsername(username).orElseThrow();

        if (me != null && !me.getId().equals(targetUser.getId())) {
            followRepository.unfollow(me.getId(), targetUser.getId());
        }

        String referer = request.getHeader("Referer");
        return referer != null ? "redirect:" + referer : "redirect:/profile/" + username;
//        return "redirect:/profile/" + username;
    }
}
