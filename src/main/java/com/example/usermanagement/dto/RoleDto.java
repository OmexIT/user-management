package com.example.usermanagement.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO for Role information Using record for immutability and automatic
 * generation of constructors, getters, equals, hashCode, and toString
 */
public record RoleDto(String name, String description, Set<String> permissions) {
	// Static factory method to create a builder
	public static RoleDtoBuilder builder() {
		return new RoleDtoBuilder();
	}

	// Builder pattern implementation
	public static class RoleDtoBuilder {
		private String name;
		private String description;
		private Set<String> permissions = new HashSet<>();

		public RoleDtoBuilder name(String name) {
			this.name = name;
			return this;
		}

		public RoleDtoBuilder description(String description) {
			this.description = description;
			return this;
		}

		public RoleDtoBuilder permissions(Set<String> permissions) {
			this.permissions = permissions;
			return this;
		}

		public RoleDtoBuilder addPermission(String permission) {
			this.permissions.add(permission);
			return this;
		}

		public RoleDto build() {
			return new RoleDto(name, description, permissions);
		}
	}
}
