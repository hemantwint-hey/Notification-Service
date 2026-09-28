package org.example.notificationservice.repository;

import org.example.notificationservice.domain.Notification;

import java.util.Optional;

public interface NotificationRepository {
    void updateStatus(Notification notification);

    Optional<Notification> findByIdempotencyKey(String idempotencyKey);

    void save(Notification notification);

    Notification findByUserId(String userId);

    Optional<Object> findById(String notificationId);
}
