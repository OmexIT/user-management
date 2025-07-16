package com.example.usermanagement.user.dto;

import com.example.usermanagement.model.enums.UserStatus;
import com.example.usermanagement.model.enums.UserType;

import java.util.Set;

public record UserDto(Long id, String externalId, String email, String displayName, UserType userType,
		UserStatus status, Set<String> roles, Set<String> permissions) {
	public static UserDtoBuilder builder() {
		return new UserDtoBuilder();
	}
	public static class UserDtoBuilder {
		private Long id;
		private String externalId;
		private String email;
		private String displayName;
		private UserType userType;
		private UserStatus status;
		private Set<String> roles;
		private Set<String> permissions;

		public UserDtoBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public UserDtoBuilder externalId(String externalId) {
			this.externalId = externalId;
			return this;
		}

		public UserDtoBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserDtoBuilder displayName(String displayName) {
			this.displayName = displayName;
			return this;
		}

		public UserDtoBuilder userType(UserType userType) {
			this.userType = userType;
			return this;
		}

		public UserDtoBuilder status(UserStatus status) {
			this.status = status;
			return this;
		}

		public UserDtoBuilder roles(Set<String> roles) {
			this.roles = roles;
			return this;
		}

		public UserDtoBuilder permissions(Set<String> permissions) {
			this.permissions = permissions;
			return this;
		}

		public UserDto build() {
			return new UserDto(id, externalId, email, displayName, userType, status, roles, permissions);
		}
	}
}
