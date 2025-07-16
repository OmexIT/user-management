package com.example.usermanagement.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_roles")
@Getter
@Setter
@NoArgsConstructor
public class UserRole {

	@Column("user_id")
	private Long userId;

	@Column("role_id")
	private Long roleId;

	@Column("granted_at")
	private LocalDateTime grantedAt;

	@Column("granted_by")
	private String grantedBy;

	@Column("expires_at")
	private LocalDateTime expiresAt;

	@Column("is_active")
	private Boolean isActive = true;

	public UserRole(Long userId, Long roleId, String grantedBy) {
		this.userId = userId;
		this.roleId = roleId;
		this.grantedBy = grantedBy;
		this.grantedAt = LocalDateTime.now();
	}

	public boolean isExpired() {
		return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
	}
}
