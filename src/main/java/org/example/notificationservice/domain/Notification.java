package org.example.notificationservice.domain;

import lombok.Builder;
import lombok.Getter;
import org.example.notificationservice.enums.NotificationPriority;
import org.example.notificationservice.enums.NotificationStatus;
import org.example.notificationservice.enums.NotificationType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Notification {

    private final String notificationId;
    private final String userId;
    private final String title;
    private final String content;
    private final NotificationType notificationType;
    private final NotificationPriority notificationPriority;
    private final String idempotencyKey;
    private final Instant createdAt;
    private NotificationStatus notificationStatus;
    private Instant updatedAt;

    /**
     * Private: there are only two ways to get a Notification.
     * create(...) for a brand new one, Notification.restore()...build() for one loaded from the database.
     */
    @Builder(builderMethodName = "restore")
    private Notification(String notificationId,
                         String userId,
                         String title,
                         String content,
                         NotificationType notificationType,
                         NotificationPriority notificationPriority,
                         NotificationStatus notificationStatus,
                         String idempotencyKey,
                         Instant createdAt,
                         Instant updatedAt) {
        this.notificationId = Objects.requireNonNull(notificationId, "notificationId");
        this.userId = Objects.requireNonNull(userId, "userId");
        this.title = Objects.requireNonNull(title, "title");
        this.content = Objects.requireNonNull(content, "content");
        this.notificationType = Objects.requireNonNull(notificationType, "notificationType");
        this.notificationPriority = Objects.requireNonNull(notificationPriority, "notificationPriority");
        this.notificationStatus = Objects.requireNonNull(notificationStatus, "notificationStatus");
        this.idempotencyKey = idempotencyKey;   // optional: null means "no deduplication"
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public static Notification create(String userId,
                                      String title,
                                      String content,
                                      NotificationType notificationType,
                                      NotificationPriority notificationPriority,
                                      String idempotencyKey,
                                      Instant now) {
        return new Notification(
                UUID.randomUUID().toString(),
                userId,
                title,
                content,
                notificationType,
                notificationPriority,
                NotificationStatus.CREATED,
                idempotencyKey,
                now,
                now);
    }

    public void transitionTo(NotificationStatus next, Instant now) {
        if (!notificationStatus.canTransition(next)) {
            throw new IllegalStateException(
                    "Notification %s cannot move from %s to %s".formatted(notificationId, notificationStatus, next));
        }
        this.notificationStatus = next;
        this.updatedAt = now;
    }
}
