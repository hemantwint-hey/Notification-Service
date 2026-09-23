package org.example.notificationservice.exception;

public class NotificationDeliveryException extends RuntimeException {
    // here retryable is used because some errors are retryable, for example the SMS service may be
    // unavailable right now but available a second later, so it is worth trying again
    private final boolean retryable;

    private NotificationDeliveryException(String message, boolean retryable, Throwable cause) {
        super(message, cause);
        this.retryable = retryable;
    }

    public static NotificationDeliveryException retryable(String message, Throwable cause) {
        return new NotificationDeliveryException(message, true, cause);
    }

    public static NotificationDeliveryException permanent(String message, Throwable cause) {
        return new NotificationDeliveryException(message, false, cause);
    }

    /** RetryPolicy asks this to decide whether another attempt makes sense. */
    public boolean isRetryable() {
        return retryable;
    }
}
