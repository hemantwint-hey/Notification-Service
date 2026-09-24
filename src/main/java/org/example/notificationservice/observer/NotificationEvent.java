package org.example.notificationservice.observer;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.enums.NotificationPriority;
import org.example.notificationservice.enums.NotificationStatus;
import org.example.notificationservice.enums.NotificationType;

import java.time.Instant;

public record NotificationEvent(
        NotificationEventType eventType,
        String notificationId,
        String userId,
        NotificationType notificationType,
        NotificationPriority priority,
        NotificationStatus status,
        String reason ,
        Instant occurredAt
        ) {
    public static NotificationEvent of(NotificationEventType eventType , Notification notification, Instant occurredAt){
        return of(eventType,notification,null,occurredAt);
    }
    public static NotificationEvent of(NotificationEventType eventType, Notification notification, String reason, Instant occurredAt){
        return new NotificationEvent(
                eventType,
                notification.getNotificationId(),
                notification.getUserId(),
                notification.getNotificationType(),
                notification.getNotificationPriority(),
                notification.getNotificationStatus(),
                reason,
                occurredAt

        );
    }

}
