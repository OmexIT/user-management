package com.example.usermanagement.dto;

/**
 * DTO for Customer information Using record for immutability and automatic
 * generation of constructors, getters, equals, hashCode, and toString
 */
public record CustomerDto(Long id, String customerNumber, String name, String type, UserDto user) {
	// Static factory method to create a builder
	public static CustomerDtoBuilder builder() {
		return new CustomerDtoBuilder();
	}

	// Builder pattern implementation
	public static class CustomerDtoBuilder {
		private Long id;
		private String customerNumber;
		private String name;
		private String type;
		private UserDto user;

		public CustomerDtoBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public CustomerDtoBuilder customerNumber(String customerNumber) {
			this.customerNumber = customerNumber;
			return this;
		}

		public CustomerDtoBuilder name(String name) {
			this.name = name;
			return this;
		}

		public CustomerDtoBuilder type(String type) {
			this.type = type;
			return this;
		}

		public CustomerDtoBuilder user(UserDto user) {
			this.user = user;
			return this;
		}

		public CustomerDto build() {
			return new CustomerDto(id, customerNumber, name, type, user);
		}
	}
}
