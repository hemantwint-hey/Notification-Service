package org.example.notificationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.notificationservice.enums.NotificationPriority;
import org.example.notificationservice.enums.NotificationType;

public record NotificationRequest(
        @NotBlank @Size(max = 64) String userId,
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 2000) String content,
        @NotNull NotificationType notificationType,
        @NotNull NotificationPriority notificationPriority,
        @Size(max = 128) String idempotencyKey) {
}
