package org.example.notificationservice.domain;

import lombok.Builder;
import lombok.Getter;
import org.example.notificationservice.enums.NotificationPriority;
import org.example.notificationservice.enums.NotificationStatus;
import org.example.notificationservice.enums.NotificationType;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Notification {
    private final String notificationId;
    private final String userId;
    private final String title;
    private final String content;
    private final NotificationType notificationType;
    private final NotificationPriority notificationPriority;
    private NotificationStatus notificationStatus;
    private final Instant cretedAt;
    private final String IdempotencyKey;
    private Instant updatedAt;

    @Builder(builderMethodName = "restore")
    public Notification(String notificationId, Instant updatedAt, NotificationPriority priority,String userId, String title, String content, NotificationType notificationType, NotificationPriority notificationPriority, Instant cretedAt, String idempotencyKey) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.notificationType = notificationType;
        this.notificationPriority = notificationPriority;
        this.cretedAt = cretedAt;
        IdempotencyKey = idempotencyKey;
        priority = notificationPriority;
        this.updatedAt = updatedAt;
    }

    public Notification(String notificationId, String userId, String content, String title, Instant now, NotificationStatus notificationStatus, NotificationPriority notificationPriority) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.notificationType = null;
        this.notificationPriority = notificationPriority;
        this.notificationStatus = notificationStatus;
        this.cretedAt = now;
        this.IdempotencyKey = null;
        this.updatedAt = now;
    }

    public static Notification create(
        String userId,
        String content,
        String title,
        Instant now,
        NotificationStatus notificationStatus,
        NotificationPriority notificationPriority
    ){
        return new Notification(UUID.randomUUID().toString(), userId, content, title, now, NotificationStatus.CREATED, notificationPriority);
    }
}
