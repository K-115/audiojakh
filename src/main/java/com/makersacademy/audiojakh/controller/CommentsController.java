package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.Comment;
import com.makersacademy.audiojakh.model.CommentLikes;
import com.makersacademy.audiojakh.model.Review;
import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.CommentLikeRepository;
import com.makersacademy.audiojakh.repository.CommentRepository;
import com.makersacademy.audiojakh.repository.ReviewRepository;
import com.makersacademy.audiojakh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentsController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository; // ADDED: Required to look up the parent review entity

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    private User currentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DefaultOidcUser oidc)) {
            return null;
        }
        String email = (String) oidc.getAttributes().get("email");
        return userRepository.findUserByEmailAddress(email).orElse(null);
    }

    @PostMapping("/comments")
    public String createComment(@RequestParam("reviewId") Long reviewId,
                                @RequestParam("content") String content,
                                @RequestParam(value = "sort", required = false, defaultValue = "recent") String sort,
                                RedirectAttributes redirectAttributes) {
        User me = currentUser();
        if (me == null) {
            return "redirect:/login";
        }

        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Comment content cannot be empty.");
            return "redirect:/reviews?sort=" + sort;
        }

        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "The review you are commenting on no longer exists.");
            return "redirect:/reviews?sort=" + sort;
        }

        try {
            Comment comment = new Comment();
            comment.setContent(content.trim());
            comment.setUser(me);
            comment.setReviewId(review.getId());
            comment.setLikes(0L);

            commentRepository.save(comment);
        } catch (Exception e) {
            System.err.println("Comment save error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while saving your comment.");
        }

        return "redirect:/reviews?sort=" + sort;
    }

    @PostMapping("/reviews/{id}/comment-likes")
    public String commentLikes(@PathVariable("id") Long commentId,
                               @RequestParam(value = "sort", required = false, defaultValue = "recent") String sort) {
        User me = currentUser();
        if (me == null) {
            return "redirect:/login";
        }

        commentLikeRepository.findByCommentIdAndUserId(commentId, me.getId()).ifPresentOrElse(
                commentLikeRepository::delete,
                () -> commentLikeRepository.save(new CommentLikes(me.getId(), commentId))
        );

        commentRepository.findById(commentId).ifPresent(c -> {
            c.setLikes((long) commentLikeRepository.countByCommentId(commentId));
            commentRepository.save(c);
        });

        return "redirect:/reviews?sort=" + sort;
    }
}
