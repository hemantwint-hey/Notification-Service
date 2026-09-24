package org.example.notificationservice.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationAuditListener implements NotificationEventListener {
    private static final Logger AUDIT = LoggerFactory.getLogger("NOTIFICATION_AUDIT");

    @Override
    public void onEvent(NotificationEvent event) {
        AUDIT.info("event={} notificationId={} userId={} channel={} priority={} status={} reason={} at={}",
                event.eventType(), event.notificationId(), event.userId(), event.notificationType(),
                event.priority(), event.status(), event.reason(), event.occurredAt());
    }
}
