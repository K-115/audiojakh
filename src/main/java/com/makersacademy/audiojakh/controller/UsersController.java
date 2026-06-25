package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
public class UsersController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/after-login")
    public RedirectView afterLogin(HttpSession session) {
        User user = new User();
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String emailAddress = principal.getEmail();

        Optional<User> existingUser = userRepository.findUserByEmailAddress(emailAddress);

        if (existingUser.isPresent()) {
            session.setAttribute("profilePicture", existingUser.get().getProfilePicture());
            session.setAttribute("userId", existingUser.get().getId());
            session.setAttribute("userUsername", existingUser.get().getUsername());
            return new RedirectView("/");

        } else {
            return new RedirectView("/sign_up");
        }
    }
}
