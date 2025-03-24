package com.example.usermanagement.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends Auditable<String> {

	@Id
	private Long id;

	@Column("customer_number")
	private String customerNumber;

	private String name;

	private String type;

	@Column("user_id")
	private Long userId;

	public Customer(String customerNumber, String name, String type, Long userId) {
		this.customerNumber = customerNumber;
		this.name = name;
		this.type = type;
		this.userId = userId;
	}
}
