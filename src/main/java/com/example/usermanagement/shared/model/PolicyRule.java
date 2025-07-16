package com.example.usermanagement.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("policy_rules")
@Getter
@Setter
@NoArgsConstructor
public class PolicyRule extends Auditable<String> {

	@Id
	private Long id;

	@Column("rule_name")
	private String ruleName;

	@Column("rule_type")
	private String ruleType;

	private String resource;

	private String action;

	@Column("condition_expression")
	private String conditionExpression;

	private String effect;

	private Integer priority = 0;

	@Column("is_active")
	private Boolean isActive = true;

	private String description;

	@Column("metadata")
	private String metadata;

	public PolicyRule(String ruleName, String ruleType, String resource, String action, String effect) {
		this.ruleName = ruleName;
		this.ruleType = ruleType;
		this.resource = resource;
		this.action = action;
		this.effect = effect;
	}

	public boolean isAllow() {
		return "ALLOW".equalsIgnoreCase(effect);
	}

	public boolean isDeny() {
		return "DENY".equalsIgnoreCase(effect);
	}
}
