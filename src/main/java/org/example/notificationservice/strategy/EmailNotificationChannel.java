package org.example.notificationservice.strategy;

import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationChannel implements  NotificationChannel {
    @Override
    public NotificationType supportedType() {
        return NotificationType.EMAIL;
    }

    @Override
    public void send(Notification notification) {
        log.info("[EMAIL] delivered notification {} to the user {}",notification.getNotificationId(),notification.getUserId());
    }
}
