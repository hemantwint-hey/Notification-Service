package org.example.notificationservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.notificationservice.enums.NotificationPriority;
import org.example.notificationservice.enums.NotificationStatus;
import org.example.notificationservice.enums.NotificationType;

import java.time.Instant;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification",
        indexes = @Index(name = "idx_notification_user_id",columnList = "user_id"),
        uniqueConstraints  = @UniqueConstraint(name = "uk_notification_idempotency_key",columnNames = "idempotency_key"))
@Builder
@Getter
public class NotificationEntity {
        @Id
        @Column(name = "notification_id",length = 36)
        private String notificationId;

        @Column(name = "user_id",nullable = false, length = 64)
        private String userId;

        @Column(nullable = false, length = 255)
        private String title;

        @Column(nullable = false, length = 4000)
        private String content;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, columnDefinition = "varchar(16)")
        private NotificationType notificationType;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, columnDefinition = "varchar(16)")
        private NotificationPriority notificationPriority;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, columnDefinition = "varchar(16)")
        private NotificationStatus notificationStatus;

        @Column(name = "idempotency_key",length = 128)
        private String idempotencyKey;

        @Column(name = "created_at",nullable = false)
        private Instant createdAt;

        @Column(name = "updated_at",nullable = false)
        private Instant updatedAt;
}
