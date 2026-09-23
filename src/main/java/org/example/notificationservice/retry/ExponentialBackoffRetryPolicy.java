package org.example.notificationservice.retry;

import org.example.notificationservice.exception.NotificationDeliveryException;

import java.time.Duration;
import java.util.Objects;

public class ExponentialBackoffRetryPolicy implements  RetryPolicy{
    private final int maxAttempts;
    private final Duration initialDelay;
    private final double multiplier;
    private final Duration maxDelay;


    public ExponentialBackoffRetryPolicy(int maxAttempts, Duration initialDelay, double multiplier, Duration maxDelay) {
        if(maxAttempts<1)throw new IllegalArgumentException("maxAttempts must be  at least 1");
        if(Objects.requireNonNull(initialDelay,"initialDelay").isNegative())throw new IllegalArgumentException("Initial delay must not be negative");
        if(multiplier<1.0)throw new IllegalArgumentException("multiplier must be at least 1.0");
        if(Objects.requireNonNull(maxDelay,"maxDelay").compareTo(initialDelay)<0)throw new IllegalArgumentException("maxDelay must be at least initialDelay");
        this.maxAttempts = maxAttempts;
        this.initialDelay = initialDelay;
        this.multiplier = multiplier;
        this.maxDelay = maxDelay;
    }

    @Override
    public int maxAttempts() {
        return maxAttempts;
    }

    @Override
    public boolean isRetryable(Throwable failure) {
        return failure instanceof NotificationDeliveryException delivery && delivery.isRetryable();
    }

    @Override
    public Duration delayAfterAttempt(int failedAttempt) {
        if(failedAttempt < 1)throw new IllegalArgumentException("failedAttempt must be at least 1");
        double millis = initialDelay.toMillis() * Math.pow(multiplier,failedAttempt - 1);

        if(millis >= maxDelay.toMillis())return maxDelay;
        return Duration.ofMillis((long) millis);
    }
}
