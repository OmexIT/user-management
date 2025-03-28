package com.example.usermanagement.controller;

import com.example.usermanagement.dto.MerchantDto;
import com.example.usermanagement.dto.MerchantUserDto;
import com.example.usermanagement.model.Merchant;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for merchant management operations Following the
 * Controller-Service-Repository pattern
 */
@RestController
@RequestMapping("/api/merchants")
@RequiredArgsConstructor
@Slf4j
public class MerchantController {

	private final MerchantService merchantService;

	/**
	 * Create a new merchant with initial user
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MerchantDto> createMerchant(@Valid @RequestBody MerchantDto merchantDto) {
		log.info("Creating merchant with code: {}", merchantDto.merchantCode());
		Merchant merchant = merchantService.createMerchant(merchantDto);

		// Build response DTO
		MerchantDto responseDto = MerchantDto.builder().id(merchant.getId()).merchantCode(merchant.getMerchantCode())
				.businessName(merchant.getBusinessName()).status(merchant.getStatus()).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	/**
	 * Get all merchants
	 */
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<MerchantDto>> getAllMerchants() {
		Iterable<Merchant> merchants = merchantService.getAllMerchants();
		List<MerchantDto> merchantDtos = new ArrayList<>();

		merchants.forEach(merchant -> {
			merchantDtos.add(MerchantDto.builder().id(merchant.getId()).merchantCode(merchant.getMerchantCode())
					.businessName(merchant.getBusinessName()).status(merchant.getStatus()).build());
		});

		return ResponseEntity.ok(merchantDtos);
	}

	/**
	 * Get merchant by ID
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
	public ResponseEntity<MerchantDto> getMerchantById(@PathVariable Long id) {
		Merchant merchant = merchantService.getMerchantById(id);

		MerchantDto merchantDto = MerchantDto.builder().id(merchant.getId()).merchantCode(merchant.getMerchantCode())
				.businessName(merchant.getBusinessName()).status(merchant.getStatus()).build();

		return ResponseEntity.ok(merchantDto);
	}

	/**
	 * Get merchant by code
	 */
	@GetMapping("/code/{merchantCode}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
	public ResponseEntity<MerchantDto> getMerchantByCode(@PathVariable String merchantCode) {
		Merchant merchant = merchantService.getMerchantByCode(merchantCode);

		MerchantDto merchantDto = MerchantDto.builder().id(merchant.getId()).merchantCode(merchant.getMerchantCode())
				.businessName(merchant.getBusinessName()).status(merchant.getStatus()).build();

		return ResponseEntity.ok(merchantDto);
	}

	/**
	 * Update merchant
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MerchantDto> updateMerchant(@PathVariable Long id,
			@Valid @RequestBody MerchantDto merchantDto) {
		Merchant updatedMerchant = merchantService.updateMerchant(id, merchantDto);

		MerchantDto responseDto = MerchantDto.builder().id(updatedMerchant.getId())
				.merchantCode(updatedMerchant.getMerchantCode()).businessName(updatedMerchant.getBusinessName())
				.status(updatedMerchant.getStatus()).build();

		return ResponseEntity.ok(responseDto);
	}

	/**
	 * Add a user to a merchant
	 */
	@PostMapping("/{id}/users")
	@PreAuthorize("hasRole('ADMIN') or (hasRole('MERCHANT') and @userContextService.isMerchantAdmin(#id))")
	public ResponseEntity<Void> addUserToMerchant(@PathVariable Long id,
			@Valid @RequestBody MerchantUserDto merchantUserDto) {
		// Ensure the merchant ID in the path matches the one in the request body
		if (!id.equals(merchantUserDto.merchantId())) {
			return ResponseEntity.badRequest().build();
		}

		User user = merchantService.addUserToMerchant(merchantUserDto);
		log.info("Added user with ID {} to merchant with ID {}", user.getId(), id);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
