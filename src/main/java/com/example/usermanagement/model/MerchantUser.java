package com.example.usermanagement.model;

import com.example.usermanagement.model.enums.MerchantUserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("merchant_users")
@Getter
@Setter
@NoArgsConstructor
public class MerchantUser extends Auditable<String> {

	@Column("merchant_id")
	private Long merchantId;

	private MerchantUserRole role;

	public MerchantUser(Long merchantId, MerchantUserRole role) {
		this.merchantId = merchantId;
		this.role = role;
	}
}
