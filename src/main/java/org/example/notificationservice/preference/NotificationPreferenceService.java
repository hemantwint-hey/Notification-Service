package org.example.notificationservice.preference;

import org.example.notificationservice.enums.NotificationType;

public interface NotificationPreferenceService {
    boolean isChannelEnabled(String userId, NotificationType notificationType);
}
