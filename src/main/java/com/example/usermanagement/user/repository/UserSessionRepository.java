package com.example.usermanagement.user.repository;

import com.example.usermanagement.user.model.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, Long> {

	Optional<UserSession> findBySessionId(String sessionId);

	Optional<UserSession> findByUserIdAndSessionId(Long userId, String sessionId);

	Iterable<UserSession> findByUserId(Long userId);

	Iterable<UserSession> findByUserIdAndIsActiveTrue(Long userId);

	Iterable<UserSession> findByIsActiveTrueAndExpiresAtBefore(LocalDateTime expiryTime);

	Iterable<UserSession> findByIpAddress(String ipAddress);

	long countByUserIdAndIsActiveTrue(Long userId);

	void deleteByUserIdAndIsActiveFalse(Long userId);
}
