package com.makersacademy.audiojakh.service;

import com.makersacademy.audiojakh.model.*;
import com.makersacademy.audiojakh.model.Notification;
import com.makersacademy.audiojakh.model.NotificationType;
import com.makersacademy.audiojakh.model.User;
import com.makersacademy.audiojakh.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional

public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createFollowNotification(User recipient, User sender) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setType(NotificationType.FOLLOW);
        notification.setMessage(sender.getUsername() + " started following you.");
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationForUser(User user) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
    }

    public long getUnreadCount(User user) {
        return notificationRepository.countByRecipientAndIsReadFalse(user);
    }

    public boolean markAsRead(Long notificationId, User currentUser) {
        return notificationRepository.findById(notificationId)
                .filter(n -> n.getRecipient().getId().equals(currentUser.getId()))
                .map(n -> { n.setRead(true); return true; })
                .orElse(false);
    }

    public void createReviewNotification(User follower, User reviewer, Review review) {
        Notification notification = new Notification();
        notification.setRecipient(follower);
        notification.setSender(reviewer);
        notification.setType(NotificationType.REVIEW);
        notification.setTargetId(review.getId());

        String itemTitle = (review.getTrack() != null ? review.getTrack().getName() : review.getAlbum().getSpotifyId());
        notification.setMessage(reviewer.getUsername() + " left a review for " + itemTitle);

        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
