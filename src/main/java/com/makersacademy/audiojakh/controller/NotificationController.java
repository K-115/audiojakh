package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.service.CurrentUserService;
import com.makersacademy.audiojakh.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping("/notifications")
    public String showNotifications(Model model) {
        User me = currentUserService.get();
        if (me == null) return "redirect:/login";

        model.addAttribute("notifications", notificationService.getNotificationForUser(me));
        return "notifications";
    }

    @PostMapping("/notifications/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        User me = currentUserService.get();
        if (me == null) return "redirect:/login";

        notificationService.markAsRead(id, me);
        return "redirect:/notifications";
    }
}
