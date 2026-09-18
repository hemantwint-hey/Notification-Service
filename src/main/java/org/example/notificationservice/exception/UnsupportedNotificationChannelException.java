package org.example.notificationservice.exception;

import org.example.notificationservice.enums.NotificationType;

public class UnsupportedNotificationChannelException extends RuntimeException {
    public UnsupportedNotificationChannelException(NotificationType notificationType){
        super("no notification channel registered for type " + notificationType);
    }
}
