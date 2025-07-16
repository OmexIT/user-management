package com.example.usermanagement.user.dto;

/**
 * DTO for Permission information Using record for immutability and automatic
 * generation of constructors, getters, equals, hashCode, and toString
 */
public record PermissionDto(String name, String description, String resource, String operation, String scope) {
	/**
	 * Factory method to create a permission with the format:
	 * OPERATION:RESOURCE:SCOPE
	 */
	public static PermissionDto fromString(String permissionString) {
		String[] parts = permissionString.split(":");
		String operation = parts.length > 0 ? parts[0] : "";
		String resource = parts.length > 1 ? parts[1] : "";
		String scope = parts.length > 2 ? parts[2] : "";

		return new PermissionDto(permissionString,
				String.format("%s permission on %s with scope %s", operation, resource, scope), resource, operation,
				scope);
	}

	/**
	 * Convert permission to string format: OPERATION:RESOURCE:SCOPE
	 */
	public String toPermissionString() {
		if (scope == null || scope.isEmpty()) {
			return String.format("%s:%s", operation, resource);
		}
		return String.format("%s:%s:%s", operation, resource, scope);
	}

	// Static factory method to create a builder
	public static PermissionDtoBuilder builder() {
		return new PermissionDtoBuilder();
	}

	// Builder pattern implementation
	public static class PermissionDtoBuilder {
		private String name;
		private String description;
		private String resource;
		private String operation;
		private String scope;

		public PermissionDtoBuilder name(String name) {
			this.name = name;
			return this;
		}

		public PermissionDtoBuilder description(String description) {
			this.description = description;
			return this;
		}

		public PermissionDtoBuilder resource(String resource) {
			this.resource = resource;
			return this;
		}

		public PermissionDtoBuilder operation(String operation) {
			this.operation = operation;
			return this;
		}

		public PermissionDtoBuilder scope(String scope) {
			this.scope = scope;
			return this;
		}

		public PermissionDto build() {
			return new PermissionDto(name, description, resource, operation, scope);
		}
	}
}
