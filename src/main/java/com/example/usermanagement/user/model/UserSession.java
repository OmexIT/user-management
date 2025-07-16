package com.example.usermanagement.user.model;

import com.example.usermanagement.shared.model.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_sessions")
@Getter
@Setter
@NoArgsConstructor
public class UserSession extends Auditable<String> {

	@Id
	private Long id;

	@Column("user_id")
	private Long userId;

	@Column("session_id")
	private String sessionId;

	@Column("ip_address")
	private String ipAddress;

	@Column("user_agent")
	private String userAgent;

	@Column("device_info")
	private String deviceInfo;

	@Column("started_at")
	private LocalDateTime startedAt;

	@Column("last_activity_at")
	private LocalDateTime lastActivityAt;

	@Column("expires_at")
	private LocalDateTime expiresAt;

	@Column("is_active")
	private Boolean isActive = true;

	@Column("terminated_at")
	private LocalDateTime terminatedAt;

	@Column("termination_reason")
	private String terminationReason;

	public UserSession(Long userId, String sessionId, String ipAddress, String userAgent) {
		this.userId = userId;
		this.sessionId = sessionId;
		this.ipAddress = ipAddress;
		this.userAgent = userAgent;
		this.startedAt = LocalDateTime.now();
		this.lastActivityAt = LocalDateTime.now();
		this.expiresAt = LocalDateTime.now().plusHours(24);
	}

	public void updateActivity() {
		this.lastActivityAt = LocalDateTime.now();
	}

	public void terminate(String reason) {
		this.isActive = false;
		this.terminatedAt = LocalDateTime.now();
		this.terminationReason = reason;
	}
}
