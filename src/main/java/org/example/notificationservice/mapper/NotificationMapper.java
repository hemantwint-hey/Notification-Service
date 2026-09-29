package org.example.notificationservice.mapper;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationEntity toEntity(Notification notification){
        return NotificationEntity.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .notificationType(notification.getNotificationType())
                .notificationPriority(notification.getNotificationPriority())
                .notificationStatus(notification.getNotificationStatus())
                .idempotencyKey(notification.getIdempotencyKey())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
    public Notification toDomain(NotificationEntity entity){
        return Notification.restore()
                .notificationId(entity.getNotificationId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .notificationType(entity.getNotificationType())
                .notificationPriority(entity.getNotificationPriority())
                .notificationStatus(entity.getNotificationStatus())
                .idempotencyKey(entity.getIdempotencyKey())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
