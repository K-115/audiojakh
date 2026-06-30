package com.makersacademy.audiojakh.repository;

import com.makersacademy.audiojakh.model.Notification;
import com.makersacademy.audiojakh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);
    long countByRecipientAndIsReadFalse(User recipient);
}
