package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Period;
import java.util.Optional;
import java.time.LocalDate;

@RestController
public class SignUpController {
    @Autowired
    UserRepository userRepository;
    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping("/sign_up")
    public ModelAndView signUp(HttpSession session){
        User user = new User();

        // Retrieve authenticated user from Auth0
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String emailAddress = principal.getEmail();

        // Check if user exists
        Optional<User> existingUser = userRepository.findUserByEmailAddress(emailAddress);
        if (existingUser.isPresent()) {
            return new ModelAndView("redirect:/");
        }

        // Setting up sign-up form
        ModelAndView signUp = new ModelAndView("sign-up");
        signUp.addObject("user", new User());
        signUp.addObject("emailAddress", emailAddress);

        // Fill first/surname from Auth0
        if (principal.getGivenName() != null) {
            signUp.addObject("firstName", principal.getGivenName());
            } else {
            signUp.addObject("firstName", "Enter First Name");
        }

        if (principal.getFamilyName() != null) {
            signUp.addObject("surname", principal.getFamilyName());
        } else {
            signUp.addObject("surname", "Enter Surname");
        }

        // Display validation errors from session
        if (session.getAttribute("namesBlank") != null) {
            signUp.addObject("namesBlank", true);
            session.setAttribute("namesBlank", null);
        }

        if (session.getAttribute("usernameBlank") != null) {
            signUp.addObject("usernameBlank", true);
            session.setAttribute("usernameBlank", null);
        }

        if (session.getAttribute("usernameExists") != null) {
            signUp.addObject("usernameExists", true);
            signUp.addObject("chosenUsername", session.getAttribute("chosenUsername"));
            session.setAttribute("usernameExists", null);
        }

        if (session.getAttribute("dobBlank") != null) {
            signUp.addObject("dobBlank", true);
            session.setAttribute("dobBlank", null);
        }

        if (session.getAttribute("underAge") != null) {
            signUp.addObject("underAge", true);
            session.setAttribute("underAge", null);
        }

        if (session.getAttribute("imageSize") != null) {
            signUp.addObject("imageSize", true);
            session.setAttribute("imageSize", null);
        }

        return signUp;
    }

    @PostMapping("/sign_up/new")
    public RedirectView create (
            @ModelAttribute User user,
            HttpSession session,
            @RequestParam("profile_picture") MultipartFile image
            ) throws IOException {

        // Retrieves authenticated user from Auth0
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // Validate username is not blank
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            session.setAttribute("usernameBlank", true);
            return new RedirectView("/sign_up");
        }

        // Validation of unique username
        Optional<User> uniqueUser = userRepository.findUserByUsername(user.getUsername());
        if (uniqueUser.isPresent()) {
            session.setAttribute("usernameExists", true);
            session.setAttribute("chosenUsername", user.getUsername());
            return new RedirectView("/sign_up");
        }

        // Validation of first/surname
        if ((user.getFirstName() == null || user.getFirstName().isBlank()) &&
                (user.getSurname() == null || user.getSurname().isBlank())) {

            // Both names blank - attempt retrieval from Auth0
            if (principal.getGivenName() != null && principal.getFamilyName() != null) {
                user.setFirstName(principal.getGivenName());
                user.setSurname(principal.getFamilyName());
            } else {
                session.setAttribute("bothNamesBlank", true);
                return new RedirectView("/sign_up");
            }

        // Only first name is blank - retrieve from Autho0
        } else if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            if (principal.getGivenName() != null) {
                user.setFirstName(principal.getGivenName());
            } else {
                session.setAttribute("firstNamesBlank", true);
                return new RedirectView("/sign_up");
            }

        // Only surname is blank - retrieve from Autho0
        } else if (user.getSurname() == null || user.getSurname().isBlank()) {
            if (principal.getFamilyName() != null) {
                user.setSurname(principal.getFamilyName());
            } else {
                session.setAttribute("surnamesBlank", true);
                return new RedirectView("/sign_up");
            }
        }

        // Validation of date of birth including 16+ verification
        if (user.getDob() == null) {
            session.setAttribute("dobBlank", true);
            return new RedirectView("/sign_up");
        }

        LocalDate today = LocalDate.now();
        int age = Period.between(user.getDob(), today). getYears();
        if (age < 16) {
            session.setAttribute("underAge", true);
            return new RedirectView("/sign_up");
        }

    // Profile pic size is capped by the 1 MB multipart limit, oversize is caught by handleOversizeImage above
        // generates unique filename
            if (!image.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(filename);
                Files.copy(
                        image.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );
                user.setProfilePicture(filename);
            } else {
                // Set default profile pic if user doesn't provide one
                user.setProfilePicture("default-avatar.png");
            }

        // Set email from Auth0
        user.setEmailAddress(principal.getEmail());

        // Save user to the database
        userRepository.save(user);

        //Retrieve saved user and store in session
        Optional<User> savedUser = userRepository.findUserByEmailAddress(user.getEmailAddress());
        if (savedUser.isPresent()) {
            session.setAttribute("profilePicture", savedUser.get().getProfilePicture());
            session.setAttribute("userId", savedUser.get().getId());
            session.setAttribute("userUsername", savedUser.get().getUsername());
        }

        return new RedirectView("/");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RedirectView handleOversizeImage(HttpSession session) {
        session.setAttribute("imageSize", true);

        return new RedirectView("/sign_up");
    }
}


