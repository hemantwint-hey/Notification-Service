package org.example.notificationservice.config;

import org.example.notificationservice.retry.ExponentialBackoffRetryPolicy;
import org.example.notificationservice.retry.RetryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;

@Configuration
public class NotificationConfig {

    @Bean
    public Clock clock(@Value("${notification.content.time-zone:UTC}") String timeZone) {
        return Clock.system(ZoneId.of(timeZone));
    }

    @Bean
    public RetryPolicy retryPolicy(@Value("${notification.retry.max-attempts:3}") int maxAttempts,
                                   @Value("${notification.retry.initial-delay:500ms}") Duration initialDelay,
                                   @Value("${notification.retry.multiplier:2.0}") double multiplier,
                                   @Value("${notification.retry.max-delay:5s}") Duration maxDelay) {
        return new ExponentialBackoffRetryPolicy(maxAttempts, initialDelay, multiplier, maxDelay);
    }
}
