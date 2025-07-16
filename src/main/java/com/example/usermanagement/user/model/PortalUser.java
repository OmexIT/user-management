package com.example.usermanagement.user.model;

import com.example.usermanagement.shared.model.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("portal_users")
@Getter
@Setter
@NoArgsConstructor
public class PortalUser extends Auditable<String> {

	@Id
	private Long id;

	@Column("user_id")
	private Long userId;

	@Column("employee_id")
	private String employeeId;

	private String department;

	@Column("access_level")
	private String accessLevel;

	@Column("can_impersonate")
	private Boolean canImpersonate = false;

	@Column("max_impersonation_duration")
	private Integer maxImpersonationDuration;

	@Column("allowed_ip_ranges")
	private String allowedIpRanges;

	@Column("metadata")
	private String metadata;

	public PortalUser(Long userId, String employeeId, String department, String accessLevel) {
		this.userId = userId;
		this.employeeId = employeeId;
		this.department = department;
		this.accessLevel = accessLevel;
	}
}
