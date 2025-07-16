package com.example.usermanagement.user.model;

import com.example.usermanagement.shared.model.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table("roles")
@Getter
@Setter
@NoArgsConstructor
public class Role extends Auditable<String> {

	@Id
	private Long id;

	@Column("role_code")
	private String roleCode;

	@Column("role_name")
	private String roleName;

	private String description;

	@Column("is_system")
	private Boolean isSystem = false;

	@Column("role_type")
	private String roleType;

	@MappedCollection(idColumn = "role_id")
	private Set<RolePermission> rolePermissions = new HashSet<>();

	public Role(String roleCode, String roleName, String description, String roleType) {
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.description = description;
		this.roleType = roleType;
	}

	public void addPermission(Permission permission) {
		rolePermissions.add(new RolePermission(permission.getId()));
	}

	public void removePermission(Long permissionId) {
		rolePermissions.removeIf(rp -> rp.getPermissionId().equals(permissionId));
	}
}
