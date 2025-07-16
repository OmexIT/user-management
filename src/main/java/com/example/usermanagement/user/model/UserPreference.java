package com.example.usermanagement.user.model;

import com.example.usermanagement.shared.model.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_preferences")
@Getter
@Setter
@NoArgsConstructor
public class UserPreference extends Auditable<String> {

	@Id
	private Long id;

	@Column("user_id")
	private Long userId;

	@Column("preference_key")
	private String preferenceKey;

	@Column("preference_value")
	private String preferenceValue;

	@Column("preference_type")
	private String preferenceType;

	private String category;

	@Column("is_sensitive")
	private Boolean isSensitive = false;

	public UserPreference(Long userId, String preferenceKey, String preferenceValue, String preferenceType,
			String category) {
		this.userId = userId;
		this.preferenceKey = preferenceKey;
		this.preferenceValue = preferenceValue;
		this.preferenceType = preferenceType;
		this.category = category;
	}
}
