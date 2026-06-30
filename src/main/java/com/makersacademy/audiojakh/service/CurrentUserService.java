package com.makersacademy.audiojakh.service;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    @Autowired
    private UserRepository userRepository;

    public User get() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }
}
