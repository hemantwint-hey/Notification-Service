package org.example.notificationservice.repository;

import org.example.notificationservice.domain.Notification;

public interface NotificationRepository {
    void updateStatus(Notification notification);
}
