package org.example.notificationservice.enums;

public enum NotificationPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public boolean overridesUserPreferences() {
        return this == CRITICAL;
    }
}
