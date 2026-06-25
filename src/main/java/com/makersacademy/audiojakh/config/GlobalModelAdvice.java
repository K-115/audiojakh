package com.makersacademy.audiojakh.config;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice
public class GlobalModelAdvice {
    @Autowired
    private UserRepository userRepository;
    @ModelAttribute("currentUsername")
    public String currentUsername(HttpSession session) {

        return (String) session.getAttribute("userUsername");
    }

    @ModelAttribute("currentProfilePicture")
    public String currentProfilePicture() {

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();


        String email = (String) principal.getAttributes().get("email");
        User user = userRepository.findUserByEmailAddress(email).orElse(null);

        if (user == null) {
            return null;
        }

        String profilePicture = user.getProfilePicture();

        return (String) profilePicture;
    }
}