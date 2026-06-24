//package com.makersacademy.audiojakh.controller;
//
//import com.makersacademy.audiojakh.model.Review;
//import com.makersacademy.audiojakh.repository.ReviewRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.view.RedirectView;
//
//@Controller
//public class ReviewsController {
//
//    @Autowired
//    ReviewRepository repository;
//
//    @GetMapping("/posts")
//    public String index(Model model) {
//        Iterable<Review> posts = repository.findAll();
//        model.addAttribute("posts", posts);
//        model.addAttribute("post", new Review());
//        return "posts/index";
//    }
//
//    @PostMapping("/posts")
//    public RedirectView create(@ModelAttribute Review post) {
//        repository.save(post);
//        return new RedirectView("/posts");
//    }
//}
