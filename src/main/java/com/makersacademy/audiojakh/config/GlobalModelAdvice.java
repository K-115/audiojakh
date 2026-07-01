package com.makersacademy.audiojakh.config;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;


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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        // Logged-out requests carry a String principal ("anonymousUser"), not a
        // DefaultOidcUser — bail out instead of casting and throwing a 500.
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser principal)) {
            return null;
        }

        String email = (String) principal.getAttributes().get("email");
        User user = userRepository.findUserByEmailAddress(email).orElse(null);
        return user == null ? null : user.getProfilePicture();
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

}