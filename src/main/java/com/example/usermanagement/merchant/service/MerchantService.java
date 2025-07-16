package com.example.usermanagement.merchant.service;

import com.example.usermanagement.user.service.UserSynchronizationService;
import com.example.usermanagement.merchant.dto.MerchantDto;
import com.example.usermanagement.merchant.dto.MerchantUserDto;
import com.example.usermanagement.user.dto.UserDto;
import com.example.usermanagement.merchant.model.Merchant;
import com.example.usermanagement.merchant.model.MerchantUser;
import com.example.usermanagement.user.model.User;
import com.example.usermanagement.model.enums.UserType;
import com.example.usermanagement.merchant.repository.MerchantRepository;
import com.example.usermanagement.user.repository.UserRepository;
import com.example.usermanagement.shared.exception.EntityExistsException;
import com.example.usermanagement.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.ws.rs.core.Response;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantService {

	private final MerchantRepository merchantRepository;
	private final UserRepository userRepository;
	private final UserSynchronizationService userSynchronizationService;
	private final RealmResource realmResource;

	/**
	 * Create a new merchant with initial user
	 */
	@Transactional
	public Merchant createMerchant(MerchantDto merchantDto) {
		// Check if merchant code already exists
		if (merchantRepository.existsByMerchantCode(merchantDto.merchantCode())) {
			throw new EntityExistsException("Merchant with code " + merchantDto.merchantCode() + " already exists");
		}

		// Create merchant
		Merchant merchant = new Merchant();
		merchant.setMerchantCode(merchantDto.merchantCode());
		merchant.setBusinessName(merchantDto.businessName());
		merchant.setStatus(merchantDto.status());

		// Save merchant to get ID
		merchant = merchantRepository.save(merchant);

		// Create initial user in Keycloak if provided
		if (merchantDto.initialUser() != null) {
			UserDto initialUserDto = merchantDto.initialUser();

			// Create user in Keycloak
			String keycloakId = createKeycloakUser(initialUserDto, "MERCHANT");

			// Synchronize user to local database
			User user = userSynchronizationService.synchronizeUser(keycloakId, initialUserDto.email(),
					initialUserDto.displayName(), UserType.MERCHANT, true, new HashMap<>());

			// Create merchant user relationship
			MerchantUser merchantUser = new MerchantUser(user.getId(), merchant.getId(), 1L); // Assuming role ID 1 for
																								// ADMIN
			user.addMerchantUser(merchantUser);

			// Save user with merchant relationship
			userRepository.save(user);
		}

		return merchant;
	}

	/**
	 * Add a user to a merchant
	 */
	@Transactional
	public User addUserToMerchant(MerchantUserDto merchantUserDto) {
		// Check if merchant exists
		Merchant merchant = merchantRepository.findById(merchantUserDto.merchantId()).orElseThrow(
				() -> new EntityNotFoundException("Merchant not found with ID: " + merchantUserDto.merchantId()));

		UserDto userDto = merchantUserDto.user();

		// Create user in Keycloak
		String keycloakId = createKeycloakUser(userDto, "MERCHANT");

		// Synchronize user to local database
		User user = userSynchronizationService.synchronizeUser(keycloakId, userDto.email(), userDto.displayName(),
				UserType.MERCHANT, true, new HashMap<>());

		// Create merchant user relationship
		MerchantUser merchantUser = new MerchantUser(user.getId(), merchant.getId(), 1L); // TODO: Map role enum to role
																							// ID
		user.addMerchantUser(merchantUser);

		// Save user with merchant relationship
		return userRepository.save(user);
	}

	/**
	 * Get merchant by ID
	 */
	public Merchant getMerchantById(Long id) {
		return merchantRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Merchant not found with ID: " + id));
	}

	/**
	 * Get merchant by code
	 */
	public Merchant getMerchantByCode(String merchantCode) {
		return merchantRepository.findByMerchantCode(merchantCode)
				.orElseThrow(() -> new EntityNotFoundException("Merchant not found with code: " + merchantCode));
	}

	/**
	 * Get all merchants
	 */
	public Iterable<Merchant> getAllMerchants() {
		return merchantRepository.findAll();
	}

	/**
	 * Update merchant
	 */
	@Transactional
	public Merchant updateMerchant(Long id, MerchantDto merchantDto) {
		Merchant merchant = getMerchantById(id);

		// Update fields
		merchant.setBusinessName(merchantDto.businessName());
		merchant.setStatus(merchantDto.status());

		return merchantRepository.save(merchant);
	}

	/**
	 * Helper method to create a user in Keycloak
	 */
	private String createKeycloakUser(UserDto userDto, String role) {
		UsersResource usersResource = realmResource.users();

		// Create user representation
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername(userDto.email());
		user.setEmail(userDto.email());
		user.setFirstName(userDto.displayName());

		// Set credentials
		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(UUID.randomUUID().toString()); // Generate random password
		credential.setTemporary(true); // Require password change on first login

		user.setCredentials(Collections.singletonList(credential));

		// Create user in Keycloak
		Response response = usersResource.create(user);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed to create user: " + response.getStatus());
		}
		String userId = CreatedResponseUtil.getCreatedId(response);

		// Assign role
		usersResource.get(userId).roles().realmLevel()
				.add(Collections.singletonList(realmResource.roles().get(role).toRepresentation()));

		return userId;
	}
}
