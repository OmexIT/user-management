package com.example.usermanagement.shared.repository;

import com.example.usermanagement.shared.model.AuditLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {

	Iterable<AuditLog> findByUserId(Long userId);

	Iterable<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);

	Iterable<AuditLog> findByAction(String action);

	Iterable<AuditLog> findByPerformedBy(String performedBy);

	Iterable<AuditLog> findByPerformedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	Iterable<AuditLog> findByUserIdAndPerformedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

	Iterable<AuditLog> findByIpAddress(String ipAddress);
}
