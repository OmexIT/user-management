package com.example.usermanagement.merchant.dto;

import com.example.usermanagement.user.dto.UserDto;
import com.example.usermanagement.model.enums.MerchantUserRole;

/**
 * DTO for MerchantUser information Using record for immutability and automatic
 * generation of constructors, getters, equals, hashCode, and toString
 */
public record MerchantUserDto(Long merchantId, MerchantUserRole role, UserDto user) {
	// Static factory method to create a builder
	public static MerchantUserDtoBuilder builder() {
		return new MerchantUserDtoBuilder();
	}

	// Builder pattern implementation
	public static class MerchantUserDtoBuilder {
		private Long merchantId;
		private MerchantUserRole role;
		private UserDto user;

		public MerchantUserDtoBuilder merchantId(Long merchantId) {
			this.merchantId = merchantId;
			return this;
		}

		public MerchantUserDtoBuilder role(MerchantUserRole role) {
			this.role = role;
			return this;
		}

		public MerchantUserDtoBuilder user(UserDto user) {
			this.user = user;
			return this;
		}

		public MerchantUserDto build() {
			return new MerchantUserDto(merchantId, role, user);
		}
	}
}
