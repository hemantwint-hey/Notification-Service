package org.example.notificationservice.retry;

import java.time.Duration;

public interface RetryPolicy {
    int maxAttempts();
    boolean isRetryable(Throwable failure);
    Duration delayAfterAttempt(int failedAttempt);
}
