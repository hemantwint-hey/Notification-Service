package org.example.notificationservice.exception;

public class NotificationDeliveryException  extends RuntimeException{
    // here retyrable is used because some error are retyrable like may be now the sms service is not available but
    // next time or second it may be available so retrable is used here
    private final boolean  retryble;
    public NotificationDeliveryException(String message, boolean retyrable , Throwable cause){
        super(message,cause);
        this.retryble = retyrable;
    }
    public static NotificationDeliveryException retryble(String message, Throwable cause){
        return new NotificationDeliveryException(message,true,cause);
    }
    public static NotificationDeliveryException permanent(String message, Throwable cause){
        return new NotificationDeliveryException(message, false, cause);
    }
}
