package com.example.usermanagement.merchant.model;

import com.example.usermanagement.shared.model.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("merchant_users")
@Getter
@Setter
@NoArgsConstructor
public class MerchantUser extends Auditable<String> {

	@Id
	private Long id;

	@Column("user_id")
	private Long userId;

	@Column("merchant_id")
	private Long merchantId;

	@Column("branch_id")
	private Long branchId;

	@Column("role_id")
	private Long roleId;

	@Column("is_primary_merchant")
	private Boolean isPrimaryMerchant;

	@Column("permissions_scope")
	private String permissionsScope;

	@Column("custom_attributes")
	private String customAttributes;

	@Column("is_active")
	private Boolean isActive;

	public MerchantUser(Long userId, Long merchantId, Long roleId) {
		this.userId = userId;
		this.merchantId = merchantId;
		this.roleId = roleId;
		this.isPrimaryMerchant = false;
		this.permissionsScope = "MERCHANT_WIDE";
		this.isActive = true;
	}

	public MerchantUser(Long userId, Long merchantId, Long branchId, Long roleId, String permissionsScope) {
		this.userId = userId;
		this.merchantId = merchantId;
		this.branchId = branchId;
		this.roleId = roleId;
		this.isPrimaryMerchant = false;
		this.permissionsScope = permissionsScope;
		this.isActive = true;
	}
}
