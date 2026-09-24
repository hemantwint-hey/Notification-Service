package org.example.notificationservice.preference;

import org.example.notificationservice.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class InMemoryNotificationPreferenceService implements NotificationPreferenceService{
    @Override
    public boolean isChannelEnabled(String userId, NotificationType notificationType) {

    }
}
