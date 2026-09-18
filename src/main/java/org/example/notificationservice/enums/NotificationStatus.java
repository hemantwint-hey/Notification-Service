package org.example.notificationservice.enums;

public enum NotificationStatus {
    CREATED,
    QUEUED,
    PROCESSING,
    SENT,
    FAILED,
    RETRYING,
    CANCELLED;

    public boolean canTransition(NotificationStatus next){
        return switch (this){
            case CREATED -> next == QUEUED || next == CANCELLED;
            case QUEUED -> next == PROCESSING || next == FAILED || next == CANCELLED;
            case PROCESSING -> next == SENT || next == RETRYING || next == FAILED;
            case RETRYING -> next == PROCESSING || next == FAILED || next == CANCELLED;
            case SENT, FAILED, CANCELLED -> false;
        };
    }
}
