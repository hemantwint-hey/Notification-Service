package org.example.notificationservice.strategy;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.enums.NotificationType;

public interface NotificationChannel {
    NotificationType supportedType();
    void send(Notification notification);

}
