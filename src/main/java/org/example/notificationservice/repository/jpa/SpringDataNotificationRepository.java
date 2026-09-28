package org.example.notificationservice.repository.jpa;

import org.example.notificationservice.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.OptionalInt;

public interface SpringDataNotificationRepository extends JpaRepository<NotificationEntity, String> {
    Optional<Notificat>

}
