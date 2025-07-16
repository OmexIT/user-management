package com.example.usermanagement.shared.repository;

import com.example.usermanagement.shared.model.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

	Iterable<Notification> findByUserId(Long userId);

	Iterable<Notification> findByUserIdAndIsReadFalse(Long userId);

	Iterable<Notification> findByUserIdAndNotificationType(Long userId, String notificationType);

	Iterable<Notification> findByUserIdAndChannel(Long userId, String channel);

	Iterable<Notification> findByIsSentFalseAndRetryCountLessThan(Integer maxRetries);

	Iterable<Notification> findByExpiresAtBeforeAndIsReadFalse(LocalDateTime expiryTime);

	long countByUserIdAndIsReadFalse(Long userId);

	void deleteByExpiresAtBefore(LocalDateTime expiryTime);
}
