package com.example.usermanagement.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("role_permissions")
@Getter
@Setter
@NoArgsConstructor
public class RolePermission {

	@Column("permission_id")
	private Long permissionId;

	public RolePermission(Long permissionId) {
		this.permissionId = permissionId;
	}
}
