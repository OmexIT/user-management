package com.example.usermanagement.merchant.dto;

import com.example.usermanagement.user.dto.UserDto;
/**
 * DTO for Merchant information Using record for immutability and automatic
 * generation of constructors, getters, equals, hashCode, and toString
 */
public record MerchantDto(Long id, String merchantCode, String businessName, String status, UserDto initialUser) {
	// Static factory method to create a builder
	public static MerchantDtoBuilder builder() {
		return new MerchantDtoBuilder();
	}

	// Builder pattern implementation
	public static class MerchantDtoBuilder {
		private Long id;
		private String merchantCode;
		private String businessName;
		private String status;
		private UserDto initialUser;

		public MerchantDtoBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public MerchantDtoBuilder merchantCode(String merchantCode) {
			this.merchantCode = merchantCode;
			return this;
		}

		public MerchantDtoBuilder businessName(String businessName) {
			this.businessName = businessName;
			return this;
		}

		public MerchantDtoBuilder status(String status) {
			this.status = status;
			return this;
		}

		public MerchantDtoBuilder initialUser(UserDto initialUser) {
			this.initialUser = initialUser;
			return this;
		}

		public MerchantDto build() {
			return new MerchantDto(id, merchantCode, businessName, status, initialUser);
		}
	}
}
