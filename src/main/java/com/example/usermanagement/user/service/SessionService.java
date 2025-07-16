package com.example.usermanagement.user.service;

import com.example.usermanagement.shared.exception.EntityNotFoundException;
import com.example.usermanagement.user.model.UserSession;
import com.example.usermanagement.user.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

	private final UserSessionRepository userSessionRepository;

	@Transactional
	public UserSession createSession(Long userId, String ipAddress, String userAgent, String deviceInfo) {
		String sessionId = UUID.randomUUID().toString();

		UserSession session = new UserSession(userId, sessionId, ipAddress, userAgent);
		session.setDeviceInfo(deviceInfo);

		return userSessionRepository.save(session);
	}

	public UserSession getSessionById(String sessionId) {
		return userSessionRepository.findBySessionId(sessionId)
				.orElseThrow(() -> new EntityNotFoundException("Session not found: " + sessionId));
	}

	@Transactional
	public UserSession updateSessionActivity(String sessionId) {
		UserSession session = getSessionById(sessionId);
		session.updateActivity();
		return userSessionRepository.save(session);
	}

	@Transactional
	public void terminateSession(String sessionId, String reason) {
		UserSession session = getSessionById(sessionId);
		session.terminate(reason);
		userSessionRepository.save(session);
	}

	@Transactional
	public void terminateAllUserSessions(Long userId, String reason) {
		Iterable<UserSession> sessions = userSessionRepository.findByUserIdAndIsActiveTrue(userId);
		sessions.forEach(session -> session.terminate(reason));
		userSessionRepository.saveAll(sessions);
	}

	public Iterable<UserSession> getActiveUserSessions(Long userId) {
		return userSessionRepository.findByUserIdAndIsActiveTrue(userId);
	}

	public long getActiveSessionCount(Long userId) {
		return userSessionRepository.countByUserIdAndIsActiveTrue(userId);
	}

	@Transactional
	@Scheduled(fixedDelay = 3600000)
	public void cleanupExpiredSessions() {
		LocalDateTime now = LocalDateTime.now();
		Iterable<UserSession> expiredSessions = userSessionRepository.findByIsActiveTrueAndExpiresAtBefore(now);

		expiredSessions.forEach(session -> session.terminate("Session expired"));
		userSessionRepository.saveAll(expiredSessions);

		log.info("Cleaned up expired sessions");
	}

	@Transactional
	public void cleanupInactiveSessions(Long userId) {
		userSessionRepository.deleteByUserIdAndIsActiveFalse(userId);
	}

	public boolean isSessionValid(String sessionId) {
		try {
			UserSession session = getSessionById(sessionId);
			return session.getIsActive() && session.getExpiresAt().isAfter(LocalDateTime.now());
		} catch (EntityNotFoundException e) {
			return false;
		}
	}

	@Transactional
	public UserSession extendSession(String sessionId, int hours) {
		UserSession session = getSessionById(sessionId);

		if (!session.getIsActive()) {
			throw new IllegalStateException("Cannot extend inactive session");
		}

		session.setExpiresAt(LocalDateTime.now().plusHours(hours));
		return userSessionRepository.save(session);
	}
}
