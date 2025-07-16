package com.example.usermanagement.user.model;

import com.example.usermanagement.merchant.model.MerchantUser;
import com.example.usermanagement.shared.model.Auditable;
import com.example.usermanagement.model.enums.UserStatus;
import com.example.usermanagement.model.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Table("users")
@Getter
@Setter
@NoArgsConstructor
public class User extends Auditable<String> {

	@Id
	private Long id;

	@Column("external_id")
	private String externalId;

	private String email;

	@Column("display_name")
	private String displayName;

	@Column("user_type")
	private UserType userType;

	private UserStatus status;

	@MappedCollection(idColumn = "user_id")
	private Set<MerchantUser> merchantUsers = new HashSet<>();

	// Helper methods
	public void addMerchantUser(MerchantUser merchantUser) {
		merchantUsers.add(merchantUser);
	}

	public void removeMerchantUser(MerchantUser merchantUser) {
		merchantUsers.remove(merchantUser);
	}
}
