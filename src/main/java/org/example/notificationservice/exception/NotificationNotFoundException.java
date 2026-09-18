package org.example.notificationservice.exception;

public class NotificationNotFoundException extends RuntimeException{
    public NotificationNotFoundException(String notificationId){
        super("Notificatin not found"+ notificationId);
    }
}
