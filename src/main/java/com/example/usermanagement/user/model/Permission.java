package com.example.usermanagement.user.model;

import com.example.usermanagement.shared.model.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission extends Auditable<String> {

	@Id
	private Long id;

	@Column("permission_code")
	private String permissionCode;

	private String resource;

	private String action;

	private String description;

	@Column("is_system")
	private Boolean isSystem = false;

	@Column("metadata")
	private String metadata;

	public Permission(String permissionCode, String resource, String action, String description) {
		this.permissionCode = permissionCode;
		this.resource = resource;
		this.action = action;
		this.description = description;
	}
}
