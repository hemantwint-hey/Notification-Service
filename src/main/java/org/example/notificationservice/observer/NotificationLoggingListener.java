package org.example.notificationservice.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationLoggingListener implements NotificationEventListener{


    @Override
    public void onEvent(NotificationEvent event) {
       if(event.eventType() == NotificationEventType.NOTIFICATION_FAILED || event.eventType() == NotificationEventType.NOTIFICATION_RETRYING){
           log.warn("{} for notification {} ({}) : {}", event.eventType(),event.notificationId(),event.notificationType(),event.reason());
       }
       else {
           log.info("{} for notification {} ({})",event.eventType(),event.notificationId(),event.notificationType());
       }
    }
}
