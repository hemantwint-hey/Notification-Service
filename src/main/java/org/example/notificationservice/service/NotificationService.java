package org.example.notificationservice.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.decorator.NotificationContent;
import org.example.notificationservice.decorator.SignatureNotificationDecorator;
import org.example.notificationservice.decorator.SimpleNotification;
import org.example.notificationservice.decorator.TimestampNotificationDecorator;
import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.dto.request.NotificationRequest;
import org.example.notificationservice.engine.NotificationEngine;
import org.example.notificationservice.enums.NotificationStatus;
import org.example.notificationservice.exception.DuplicateIdempotencyKeyException;
import org.example.notificationservice.exception.InvalidNotificationRequestException;
import org.example.notificationservice.exception.NotificationNotFoundException;
import org.example.notificationservice.observer.NotificationEvent;
import org.example.notificationservice.observer.NotificationEventPublisher;
import org.example.notificationservice.observer.NotificationEventType;
import org.example.notificationservice.preference.NotificationPreferenceService;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;




@Slf4j
@Service
public class NotificationService {
    private final NotificationRepository repository;
    private final NotificationEngine engine;
    private final NotificationEventPublisher eventPublisher;
    private final NotificationPreferenceService preferenceService;
    private final Validator validator;
    private final Clock clock;
    private final String signature;

    public NotificationService(NotificationRepository repository,NotificationPreferenceService preferenceService, NotificationEngine engine,NotificationEventPublisher eventPublisher, Validator validator, Clock clock,
                               @Value("${notification.content.signature:}") String signature) {
        this.repository = repository;
        this.engine = engine;
        this.eventPublisher = eventPublisher;
        this.preferenceService = preferenceService;
        this.validator = validator;
        this.clock = clock;
        this.signature = signature;
    }

    public Notification send(NotificationRequest request){
        validate(request);
        String idempotencyKey = StringUtils.hasText(request.idempotencyKey())? request.idempotencyKey() : null;
        if(idempotencyKey != null){
            Optional<Notification> existing = repository.findByIdempotencyKey(idempotencyKey);
            if(existing.isPresent()){
                log.info("Idempotency key already used, returning notification {}", existing.get().getNotificationId());
                return existing.get();
            }
        }

        NotificationContent content = composeContent(request);
        Notification notification = Notification.create(
                request.userId(),
                content.getTitle(),
                content.getBody(),
                request.notificationType(),
                request.notificationPriority(),
                idempotencyKey,
                clock.instant()
        );
        try{
            repository.save(notification);
        }
        catch (DuplicateIdempotencyKeyException ex){
            return repository.findByIdempotencyKey(idempotencyKey).orElseThrow(() -> ex);
        }
        eventPublisher.publish(NotificationEvent.of(NotificationEventType.NOTIFICATION_CREATED, notification, clock.instant()));
        if(!isDeliveryAllowed(notification)){
            changeStatus(notification,NotificationStatus.CANCELLED);
            eventPublisher.publish(NotificationEvent.of(NotificationEventType.NOTIFICATION_CANCELLED,notification,"Channel %s disabled by user".formatted(notification.getNotificationType()),clock.instant()));
            return notification;
        }

        changeStatus(notification, NotificationStatus.QUEUED);
        engine.deliver(notification);
        return notification;
    }
    public List<Notification> getByUserId(String userId){
        return repository.findByUserId(userId);
    }

   public Notification getById(String notificationId){
        return repository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
   }
    private void validate(NotificationRequest request){
        if(request == null)throw new InvalidNotificationRequestException(List.of("request must not be null"));
        Set<ConstraintViolation<NotificationRequest>> violations = validator.validate(request);
        if(!violations.isEmpty()){
            List<String> messages = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .sorted()
                    .toList();
            throw new InvalidNotificationRequestException(messages);
        }
    }

    private NotificationContent composeContent(NotificationRequest request){
        NotificationContent content = new SimpleNotification(request.title(), request.content());
        content = new TimestampNotificationDecorator(content, ZonedDateTime.now(clock));
        if(StringUtils.hasText(signature)){
            content = new SignatureNotificationDecorator(content, signature);
        }
        return content;
    }
    private boolean isDeliveryAllowed(Notification notification){
        return notification.getNotificationPriority().overridesUserPreferences()
                || preferenceService.isChannelEnabled(notification.getUserId(), notification.getNotificationType());
    }

    private void changeStatus(Notification notification, NotificationStatus status){
        notification.transitionTo(status, clock.instant());
        repository.updateStatus(notification);
    }
}
