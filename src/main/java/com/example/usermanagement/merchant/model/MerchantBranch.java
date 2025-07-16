package com.example.usermanagement.merchant.model;

import com.example.usermanagement.shared.model.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("merchant_branches")
@Getter
@Setter
@NoArgsConstructor
public class MerchantBranch extends Auditable<String> {

	@Id
	private Long id;

	@Column("merchant_id")
	private Long merchantId;

	@Column("parent_branch_id")
	private Long parentBranchId;

	@Column("branch_code")
	private String branchCode;

	@Column("branch_name")
	private String branchName;

	@Column("branch_type")
	private String branchType;

	private String address;

	@Column("phone_number")
	private String phoneNumber;

	private String email;

	@Column("manager_name")
	private String managerName;

	@Column("is_active")
	private Boolean isActive;

	private String metadata;

	public MerchantBranch(Long merchantId, String branchCode, String branchName, String branchType) {
		this.merchantId = merchantId;
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.branchType = branchType;
		this.isActive = true;
	}

	public MerchantBranch(Long merchantId, Long parentBranchId, String branchCode, String branchName,
			String branchType) {
		this(merchantId, branchCode, branchName, branchType);
		this.parentBranchId = parentBranchId;
	}
}
