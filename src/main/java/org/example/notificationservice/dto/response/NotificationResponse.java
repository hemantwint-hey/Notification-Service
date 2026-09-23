package org.example.notificationservice.dto.response;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.enums.NotificationPriority;
import org.example.notificationservice.enums.NotificationStatus;
import org.example.notificationservice.enums.NotificationType;

import java.time.Instant;

public record NotificationResponse(
        String notificationId,
        String userId,
        String title,
        String content,
        NotificationType notificationType,
        NotificationPriority notificationPriority,
        NotificationStatus notificationStatus,
        String idempotencyKey,
        Instant createdAt,
        Instant updatedAt) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getNotificationId(),
                notification.getUserId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getNotificationType(),
                notification.getNotificationPriority(),
                notification.getNotificationStatus(),
                notification.getIdempotencyKey(),
                notification.getCreatedAt(),
                notification.getUpdatedAt());
    }
}
