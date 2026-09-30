package org.example.notificationservice.retry;

import org.example.notificationservice.exception.NotificationDeliveryException;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class ExponentialBackoffRetryPolicyTest {
    private final ExponentialBackoffRetryPolicy policy = new ExponentialBackoffRetryPolicy(
            5, Duration.ofMillis(100), 2.0, Duration.ofSeconds(1)
    );
    @Test
    void  delayDoublesAfterEachFailureCapped(){
        assertThat(policy.delayAfterAttempt(1)).isEqualTo(Duration.ofMillis(100));
        assertThat(policy.delayAfterAttempt(2)).isEqualTo(Duration.ofMillis(200));
        assertThat(policy.delayAfterAttempt(3)).isEqualTo(Duration.ofMillis(400));
        assertThat(policy.delayAfterAttempt(4)).isEqualTo(Duration.ofMillis(800));
        assertThat(policy.delayAfterAttempt(5)).isEqualTo(Duration.ofSeconds(1));
        assertThat(policy.delayAfterAttempt(5000)).isEqualTo(Duration.ofSeconds(1));
    }

    void onlyRetryableDeliveryFailureAreRetried(){
        assertThat(policy.isRetryable(NotificationDeliveryException.retryable("gateway timeout ",null))).isTrue();
        assertThat(policy.isRetryable(NotificationDeliveryException.permanent("invalid number ",null))).isTrue();
        assertThat(policy.isRetryable(new IllegalArgumentException())).isFalse();

    }

}
