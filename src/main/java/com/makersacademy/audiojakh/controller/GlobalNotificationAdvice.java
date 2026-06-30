package com.makersacademy.audiojakh.controller;

import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.service.CurrentUserService;
import com.makersacademy.audiojakh.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class GlobalNotificationAdvice {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CurrentUserService currentUserService;

    @ModelAttribute
    public void addUnreadFlag(Model model) {
        User me = currentUserService.get();
        model.addAttribute("hasUnread", me != null && notificationService.getUnreadCount(me) > 0);
    }
}
