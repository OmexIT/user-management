package com.example.usermanagement.service;

import com.example.usermanagement.model.User;
import com.example.usermanagement.model.enums.UserStatus;
import com.example.usermanagement.model.enums.UserType;
import com.example.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizationService {

	private final UserRepository userRepository;

	/**
	 * Synchronize user from Keycloak to local database
	 */
	@Transactional
	public User synchronizeUser(String keycloakId, String email, String displayName, UserType userType, boolean enabled,
			Map<String, Object> attributes) {
		// Find user in local database or create new one
		User user = userRepository.findByExternalId(keycloakId).orElse(new User());

		// Update user data
		user.setExternalId(keycloakId);
		user.setEmail(email);
		user.setDisplayName(displayName);
		user.setUserType(userType);

		// Set status
		user.setStatus(enabled ? UserStatus.ACTIVE : UserStatus.INACTIVE);

		// Save user
		return userRepository.save(user);
	}

	/**
	 * Find user by external ID
	 */
	public User findByExternalId(String externalId) {
		return userRepository.findByExternalId(externalId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with external ID: " + externalId));
	}

	/**
	 * Find user by email
	 */
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Check if user exists by external ID
	 */
	public boolean existsByExternalId(String externalId) {
		return userRepository.existsByExternalId(externalId);
	}
}
