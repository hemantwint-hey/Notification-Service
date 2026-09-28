package org.example.notificationservice.repository;

import org.example.notificationservice.domain.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    void updateStatus(Notification notification);

    Optional<Notification> findByIdempotencyKey(String idempotencyKey);

    void save(Notification notification);

    List<Notification> findByUserId(String userId);

    Optional<Notification> findById(String notificationId);
}
