package com.example.usermanagement.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

	@Id
	private Long id;

	@Column("user_id")
	private Long userId;

	@Column("entity_type")
	private String entityType;

	@Column("entity_id")
	private String entityId;

	private String action;

	@Column("old_value")
	private String oldValue;

	@Column("new_value")
	private String newValue;

	@Column("ip_address")
	private String ipAddress;

	@Column("user_agent")
	private String userAgent;

	@Column("performed_at")
	private LocalDateTime performedAt;

	@Column("performed_by")
	private String performedBy;

	private String context;

	public AuditLog(Long userId, String entityType, String entityId, String action, String performedBy) {
		this.userId = userId;
		this.entityType = entityType;
		this.entityId = entityId;
		this.action = action;
		this.performedBy = performedBy;
		this.performedAt = LocalDateTime.now();
	}
}
