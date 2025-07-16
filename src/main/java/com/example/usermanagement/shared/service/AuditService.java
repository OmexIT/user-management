package com.example.usermanagement.shared.service;

import com.example.usermanagement.user.service.UserContextService;
import com.example.usermanagement.shared.model.AuditLog;
import com.example.usermanagement.shared.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

	private final AuditLogRepository auditLogRepository;
	private final UserContextService userContextService;

	@Transactional
	public void logAction(Long userId, String entityType, String entityId, String action, String oldValue,
			String newValue, HttpServletRequest request) {

		AuditLog auditLog = new AuditLog(userId, entityType, entityId, action,
				userContextService.getCurrentUserPrincipal().getSubject());

		auditLog.setOldValue(oldValue);
		auditLog.setNewValue(newValue);

		if (request != null) {
			auditLog.setIpAddress(getClientIpAddress(request));
			auditLog.setUserAgent(request.getHeader("User-Agent"));
		}

		auditLogRepository.save(auditLog);
	}

	@Transactional
	public void logSimpleAction(String entityType, String entityId, String action, HttpServletRequest request) {
		Long userId = userContextService.getCurrentUser().getId();
		logAction(userId, entityType, entityId, action, null, null, request);
	}

	public Iterable<AuditLog> getUserAuditLogs(Long userId) {
		return auditLogRepository.findByUserId(userId);
	}

	public Iterable<AuditLog> getUserAuditLogsBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
		return auditLogRepository.findByUserIdAndPerformedAtBetween(userId, startDate, endDate);
	}

	public Iterable<AuditLog> getEntityAuditLogs(String entityType, String entityId) {
		return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
	}

	public Iterable<AuditLog> getAuditLogsByAction(String action) {
		return auditLogRepository.findByAction(action);
	}

	public Iterable<AuditLog> getAuditLogsByPerformer(String performedBy) {
		return auditLogRepository.findByPerformedBy(performedBy);
	}

	public Iterable<AuditLog> getAuditLogsBetween(LocalDateTime startDate, LocalDateTime endDate) {
		return auditLogRepository.findByPerformedAtBetween(startDate, endDate);
	}

	private String getClientIpAddress(HttpServletRequest request) {
		String xForwardedForHeader = request.getHeader("X-Forwarded-For");
		if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
			return xForwardedForHeader.split(",")[0].trim();
		}

		String xRealIpHeader = request.getHeader("X-Real-IP");
		if (xRealIpHeader != null && !xRealIpHeader.isEmpty()) {
			return xRealIpHeader;
		}

		return request.getRemoteAddr();
	}

	@Transactional
	public void logAuthenticationEvent(Long userId, String action, boolean success, String ipAddress, String userAgent,
			String context) {

		String performedBy = success ? String.valueOf(userId) : "ANONYMOUS";
		AuditLog auditLog = new AuditLog(userId, "AUTHENTICATION", String.valueOf(userId), action, performedBy);

		auditLog.setIpAddress(ipAddress);
		auditLog.setUserAgent(userAgent);
		auditLog.setContext(context);
		auditLog.setNewValue(success ? "SUCCESS" : "FAILURE");

		auditLogRepository.save(auditLog);
	}

	@Transactional
	public void logPermissionCheck(Long userId, String resource, String action, boolean granted, String context) {
		AuditLog auditLog = new AuditLog(userId, "PERMISSION_CHECK", resource, action, String.valueOf(userId));

		auditLog.setNewValue(granted ? "GRANTED" : "DENIED");
		auditLog.setContext(context);

		auditLogRepository.save(auditLog);
	}

	@Transactional
	public void logDataAccess(Long userId, String entityType, String entityId, String operation, String context) {
		AuditLog auditLog = new AuditLog(userId, "DATA_ACCESS", entityId, operation, String.valueOf(userId));

		auditLog.setContext(context);

		auditLogRepository.save(auditLog);
	}
}
