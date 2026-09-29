package org.example.notificationservice.repository.jpa;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.exception.DuplicateIdempotencyKeyException;
import org.example.notificationservice.exception.NotificationNotFoundException;
import org.example.notificationservice.mapper.NotificationMapper;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaNotificationRepository implements NotificationRepository {

    private final SpringDataNotificationRepository springDataRepository;
    private final NotificationMapper mapper;

    public JpaNotificationRepository(SpringDataNotificationRepository springDataRepository, NotificationMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public void updateStatus(Notification notification) {
        int updatedRows = springDataRepository.updateStatus(
                notification.getNotificationId(), notification.getNotificationStatus(), notification.getUpdatedAt()
        );
        if(updatedRows == 0)throw new NotificationNotFoundException(notification.getNotificationId());

    }

    @Override
    public Optional<Notification> findByIdempotencyKey(String idempotencyKey) {
        return springDataRepository.findByIdempotencyKey(idempotencyKey).map(mapper::toDomain);
    }

    @Override
    public void save(Notification notification) {
        try{
            springDataRepository.saveAndFlush(mapper.toEntity(notification));
        }
        catch (DataIntegrityViolationException ex){
            String key  = notification.getIdempotencyKey();
            if(key != null &&   springDataRepository.existsByIdempotencyKey(key) )
                throw new DuplicateIdempotencyKeyException(key, ex);
            throw ex;
        }
    }

    @Override
    public List<Notification> findByUserId(String userId) {
        return  springDataRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toDomain)
                .toList();

    }

    @Override
    public Optional<Notification> findById(String notificationId) {
        return springDataRepository.findById(notificationId).map(mapper::toDomain);
    }
}
