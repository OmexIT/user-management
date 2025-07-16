package com.example.usermanagement.user.service;

import com.example.usermanagement.user.model.User;
import com.example.usermanagement.model.enums.UserType;
import com.example.usermanagement.shared.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserContextService {

	private final UserSynchronizationService userSynchronizationService;

	/**
	 * Get current user principal from security context
	 */
	public UserPrincipal getCurrentUserPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
			throw new AccessDeniedException("No authenticated user found");
		}
		return (UserPrincipal) authentication.getPrincipal();
	}

	/**
	 * Get current user from database, synchronizing if necessary
	 */
	@Transactional
	public User getCurrentUser() {
		UserPrincipal principal = getCurrentUserPrincipal();
		String externalId = principal.getSubject();

		try {
			// Try to find user in database
			return userSynchronizationService.findByExternalId(externalId);
		} catch (Exception e) {
			// If user doesn't exist, determine user type from roles
			UserType userType = determineUserType(principal.getRoles());

			// Synchronize from Keycloak
			return userSynchronizationService.synchronizeUser(externalId, principal.getEmail(), principal.getEmail(), // Use
																														// email
																														// as
																														// display
																														// name
																														// if
																														// not
																														// available
					userType, true, principal.getAttributes());
		}
	}

	/**
	 * Get current user's roles
	 */
	public Set<String> getCurrentUserRoles() {
		return getCurrentUserPrincipal().getRoles();
	}

	/**
	 * Get current user's permissions
	 */
	public Set<String> getCurrentUserPermissions() {
		return getCurrentUserPrincipal().getPermissions();
	}

	/**
	 * Check if current user has a specific role
	 */
	public boolean hasRole(String role) {
		return getCurrentUserPrincipal().hasRole(role);
	}

	/**
	 * Check if current user has a specific permission
	 */
	public boolean hasPermission(String permission) {
		return getCurrentUserPrincipal().hasPermission(permission);
	}

	/**
	 * Determine user type from roles
	 */
	private UserType determineUserType(Set<String> roles) {
		if (roles.contains("CUSTOMER")) {
			return UserType.CUSTOMER;
		} else if (roles.contains("MERCHANT")) {
			return UserType.MERCHANT;
		} else if (roles.contains("ADMIN")) {
			return UserType.PORTAL;
		}

		// Default
		return UserType.PORTAL;
	}
}
