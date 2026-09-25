package org.example.notificationservice.engine;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.enums.NotificationStatus;
import org.example.notificationservice.exception.UnsupportedNotificationChannelException;
import org.example.notificationservice.factory.NotificationChannelResolver;
import org.example.notificationservice.observer.NotificationEvent;
import org.example.notificationservice.observer.NotificationEventPublisher;
import org.example.notificationservice.observer.NotificationEventType;
import org.example.notificationservice.repository.NotificationRepository;
import org.example.notificationservice.retry.RetryPolicy;
import org.example.notificationservice.strategy.NotificationChannel;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;

@Component
public class NotificationEngine {
    private final NotificationChannelResolver channelResolver;
    private final NotificationRepository repository;
    private final NotificationEventPublisher eventPublisher;
    private final RetryPolicy retryPolicy;
    private final Clock clock;

    public NotificationEngine(NotificationChannelResolver channelResolver, NotificationRepository repository, NotificationEventPublisher eventPublisher, RetryPolicy retryPolicy, Clock clock) {
        this.channelResolver = channelResolver;
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.retryPolicy = retryPolicy;
        this.clock = clock;
    }
    public void deliver(Notification notification){
        NotificationChannel channel;
        try{
            channel = channelResolver.resolve(notification.getNotificationType());
        }catch(UnsupportedNotificationChannelException ex){
            markFailed(notification , ex);
            return;
        }
        for( int attempt = 1; ; attempt++){
            changeStatus(notification,NotificationStatus.PROCESSING);
            RuntimeException failure = trySend(channel,notification);
            if(failure == null){
                changeStatus(notification , NotificationStatus.SENT);
                publish(NotificationEventType.NOTIFICATION_SENT,notification, null);
                return ;
            }
            boolean attemptsLeft = attempt < retryPolicy.maxAttempts();
            if( !attemptsLeft || !retryPolicy.isRetryable(failure)){
                markFailed(notification,failure);
                return;
            }
            changeStatus(notification, NotificationStatus.RETRYING);
            publish(NotificationEventType.NOTIFICATION_RETRYING,notification,reasonOf(failure));
            if(!waitBeforeRetry(retryPolicy.delayAfterAttempt(attempt))){
                markFailed(notification, failure);
                return;
            }
        }
    }
    private RuntimeException trySend(NotificationChannel channel, Notification notification){
        try{
            channel.send(notification);
            return null;
        }
        catch (RuntimeException ex){
            return ex;
        }
    }
    private boolean waitBeforeRetry(Duration delay){
        if(delay.isZero() || delay.isNegative())return true;
        try {
            Thread.sleep(delay.toMillis());
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void markFailed(Notification notification, RuntimeException ex) {
        changeStatus(notification,NotificationStatus.FAILED);
        publish(NotificationEventType.NOTIFICATION_FAILED, notification, reasonOf(ex));
    }
    private void changeStatus(Notification notification, NotificationStatus status){
        notification.transitionTo(status, clock.instant());
        repository.updateStatus(notification);
    }
    private void publish(NotificationEventType type , Notification notification , String reason ){
        eventPublisher.publish(NotificationEvent.of(type,notification,reason, clock.instant()));
    }
    private static String  reasonOf(RuntimeException failure){
        return failure.getMessage() != null ?failure.getMessage() : failure.getClass().getSimpleName();
    }
}
