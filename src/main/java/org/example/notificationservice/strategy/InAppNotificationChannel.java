package org.example.notificationservice.strategy;

import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.enums.NotificationType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InAppNotificationChannel implements NotificationChannel{
    @Override
    public NotificationType supportedType() {
        return NotificationType.IN_APP;
    }

    @Override
    public void send(Notification notification) {
        log.info("[IN-APP] delivered notification {} to the user {}",notification.getNotificationId(),notification.getUserId());
    }
}
