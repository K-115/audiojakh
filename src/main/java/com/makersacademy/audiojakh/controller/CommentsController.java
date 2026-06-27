package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Comment;
import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.CommentRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentsController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) return null;
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @PostMapping("/comments")
    public String createComment(@RequestParam("reviewId") Long reviewId,
                                @RequestParam("content") String content,
                                RedirectAttributes redirectAttributes) {
        User me = currentUser();
        if (me == null) {
            return "redirect:/login";
        }

        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Comment content cannot be empty.");
            return "redirect:/reviews";
        }

        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setPoster(me.getId());
            comment.setReviewId(reviewId);
            comment.setLikes(0L);

            commentRepository.save(comment);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "You have already commented on this review!");
        }

        return "redirect:/reviews";
    }
}
