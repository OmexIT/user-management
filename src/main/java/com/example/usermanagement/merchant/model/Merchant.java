package com.example.usermanagement.merchant.model;

import com.example.usermanagement.shared.model.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("merchants")
@Getter
@Setter
@NoArgsConstructor
public class Merchant extends Auditable<String> {

	@Id
	private Long id;

	@Column("merchant_code")
	private String merchantCode;

	@Column("business_name")
	private String businessName;

	private String status;

	public Merchant(String merchantCode, String businessName, String status) {
		this.merchantCode = merchantCode;
		this.businessName = businessName;
		this.status = status;
	}
}
