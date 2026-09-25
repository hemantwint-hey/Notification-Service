package org.example.notificationservice.preference;

import org.example.notificationservice.enums.NotificationType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryNotificationPreferenceService implements NotificationPreferenceService{

    private final Map<String, Set<NotificationType>> disabledChannelsByUser  = new ConcurrentHashMap<>();
    @Override
    public boolean isChannelEnabled(String userId, NotificationType notificationType) {
        return !disabledChannelsByUser.getOrDefault(userId, Set.of()).contains(notificationType);
    }
    public void setChannelEnabled(String userId, NotificationType notificationType, boolean enabled){
        disabledChannelsByUser.compute(userId, (id, disabled) -> {
            Set<NotificationType> updated = disabled == null
                    ? EnumSet.noneOf(NotificationType.class)
                    : EnumSet.copyOf(disabled);
            if (enabled) {
                updated.remove(notificationType);
            } else {
                updated.add(notificationType);
            }
            return updated.isEmpty() ? null : updated;   // returning null removes the entry
        });
    }
}
