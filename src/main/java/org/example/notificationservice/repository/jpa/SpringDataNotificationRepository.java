package org.example.notificationservice.repository.jpa;

import jakarta.transaction.Transactional;
import org.example.notificationservice.entity.NotificationEntity;
import org.example.notificationservice.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface SpringDataNotificationRepository extends JpaRepository<NotificationEntity, String> {
    Optional<NotificationEntity> findByIdempotencyKey(String idempotencyKey);
    boolean existByIdempotencyKey(String idempotencyKey);
    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(""" 

            update NotificationEntity n
                                       set n.status = :status, n.updatedAt = :updatedAt
                                       where n.notificationId = :notificationId
""")
    int  updateStatus(@Param("notificationId")String notificationId,
                      @Param("status")NotificationStatus status,
                      @Param("updatedAt")Instant updatedAt);

}
