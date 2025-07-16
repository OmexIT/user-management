package com.example.usermanagement.shared.service;

import com.example.usermanagement.user.service.UserContextService;
import com.example.usermanagement.shared.exception.EntityNotFoundException;
import com.example.usermanagement.shared.model.Notification;
import com.example.usermanagement.user.model.User;
import com.example.usermanagement.shared.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserContextService userContextService;

	@Transactional
	public Notification createNotification(Long userId, String type, String channel, String subject, String content) {
		Notification notification = new Notification(userId, type, channel, subject, content);
		return notificationRepository.save(notification);
	}

	@Transactional
	public Notification createNotificationWithMetadata(Long userId, String type, String channel, String subject,
			String content, String metadata) {
		Notification notification = createNotification(userId, type, channel, subject, content);
		notification.setMetadata(metadata);
		return notificationRepository.save(notification);
	}

	public Notification getNotificationById(Long id) {
		return notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification", id));
	}

	@Transactional
	public Notification markAsRead(Long notificationId) {
		Notification notification = getNotificationById(notificationId);
		notification.markAsRead();
		return notificationRepository.save(notification);
	}

	@Transactional
	public void markAllAsRead(Long userId) {
		Iterable<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
		unreadNotifications.forEach(Notification::markAsRead);
		notificationRepository.saveAll(unreadNotifications);
	}

	public Iterable<Notification> getUserNotifications(Long userId) {
		return notificationRepository.findByUserId(userId);
	}

	public Iterable<Notification> getUnreadNotifications(Long userId) {
		return notificationRepository.findByUserIdAndIsReadFalse(userId);
	}

	public long getUnreadCount(Long userId) {
		return notificationRepository.countByUserIdAndIsReadFalse(userId);
	}

	@Async
	@Transactional
	public CompletableFuture<Void> sendNotification(Long notificationId) {
		try {
			Notification notification = getNotificationById(notificationId);

			switch (notification.getChannel()) {
				case "EMAIL" :
					sendEmailNotification(notification);
					break;
				case "SMS" :
					sendSmsNotification(notification);
					break;
				case "PUSH" :
					sendPushNotification(notification);
					break;
				case "IN_APP" :
					break;
				default :
					log.warn("Unknown notification channel: {}", notification.getChannel());
			}

			notification.markAsSent();
			notificationRepository.save(notification);

		} catch (Exception e) {
			handleNotificationError(notificationId, e);
		}

		return CompletableFuture.completedFuture(null);
	}

	private void sendEmailNotification(Notification notification) {
		log.info("Sending email notification to user {}: {}", notification.getUserId(), notification.getSubject());
	}

	private void sendSmsNotification(Notification notification) {
		log.info("Sending SMS notification to user {}: {}", notification.getUserId(), notification.getContent());
	}

	private void sendPushNotification(Notification notification) {
		log.info("Sending push notification to user {}: {}", notification.getUserId(), notification.getSubject());
	}

	@Transactional
	private void handleNotificationError(Long notificationId, Exception error) {
		try {
			Notification notification = getNotificationById(notificationId);
			notification.setRetryCount(notification.getRetryCount() + 1);
			notification.setErrorMessage(error.getMessage());
			notificationRepository.save(notification);
		} catch (Exception e) {
			log.error("Failed to update notification error status", e);
		}
	}

	@Scheduled(fixedDelay = 300000)
	@Transactional
	public void retryFailedNotifications() {
		int maxRetries = 3;
		Iterable<Notification> failedNotifications = notificationRepository
				.findByIsSentFalseAndRetryCountLessThan(maxRetries);

		for (Notification notification : failedNotifications) {
			sendNotification(notification.getId());
		}
	}

	@Scheduled(cron = "0 0 2 * * ?")
	@Transactional
	public void cleanupExpiredNotifications() {
		LocalDateTime cutoffDate = LocalDateTime.now();
		notificationRepository.deleteByExpiresAtBefore(cutoffDate);
		log.info("Cleaned up expired notifications");
	}

	@Transactional
	public void notifyUser(User user, String type, String subject, String content) {
		createNotification(user.getId(), type, "IN_APP", subject, content);

		CompletableFuture.runAsync(() -> {
			try {
				Notification emailNotification = createNotification(user.getId(), type, "EMAIL", subject, content);
				sendNotification(emailNotification.getId());
			} catch (Exception e) {
				log.error("Failed to send email notification", e);
			}
		});
	}

	@Transactional
	public void broadcastNotification(String type, String subject, String content, String userType) {
		log.info("Broadcasting notification to all {} users", userType);
	}
}
