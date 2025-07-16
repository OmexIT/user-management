package com.example.usermanagement.user.controller;

import com.example.usermanagement.user.dto.PermissionCheckDto;
import com.example.usermanagement.user.dto.UserDto;
import com.example.usermanagement.user.mapper.UserMapper;
import com.example.usermanagement.user.model.User;
import com.example.usermanagement.user.service.UserContextService;
import com.example.usermanagement.user.service.UserSynchronizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user-related operations Following the
 * Controller-Service-Repository pattern
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final UserContextService userContextService;
	private final UserSynchronizationService userSynchronizationService;
	private final UserMapper userMapper;

	/**
	 * Get current user information
	 */
	@GetMapping("/me")
	public ResponseEntity<UserDto> getCurrentUser() {
		User user = userContextService.getCurrentUser();
		UserDto userDTO = userMapper.toDto(user, userContextService.getCurrentUserRoles(),
				userContextService.getCurrentUserPermissions());
		return ResponseEntity.ok(userDTO);
	}

	/**
	 * Get user by ID (admin only)
	 */
	@GetMapping("/admin/users/{externalId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDto> getUserByExternalId(@PathVariable String externalId) {
		User user = userSynchronizationService.findByExternalId(externalId);
		UserDto userDto = userMapper.toDto(user);
		return ResponseEntity.ok(userDto);
	}

	/**
	 * Check if current user has a specific permission
	 */
	@GetMapping("/permissions/check")
	public ResponseEntity<PermissionCheckDto> checkPermission(@RequestParam String permission) {
		boolean hasPermission = userContextService.hasPermission(permission);
		return ResponseEntity.ok(new PermissionCheckDto(permission, hasPermission));
	}

	/**
	 * Customer-specific endpoint
	 */
	@GetMapping("/customers/dashboard")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<String> customerDashboard() {
		return ResponseEntity
				.ok("Customer Dashboard - Welcome " + userContextService.getCurrentUser().getDisplayName());
	}

	/**
	 * Merchant-specific endpoint
	 */
	@GetMapping("/merchants/dashboard")
	@PreAuthorize("hasRole('MERCHANT')")
	public ResponseEntity<String> merchantDashboard() {
		return ResponseEntity
				.ok("Merchant Dashboard - Welcome " + userContextService.getCurrentUser().getDisplayName());
	}

	/**
	 * Admin-specific endpoint
	 */
	@GetMapping("/admin/dashboard")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> adminDashboard() {
		return ResponseEntity.ok("Admin Dashboard - Welcome " + userContextService.getCurrentUser().getDisplayName());
	}

	/**
	 * Permission-based endpoint
	 */
	@GetMapping("/resources")
	@PreAuthorize("hasPermission('READ:RESOURCE', 'RESOURCE')")
	public ResponseEntity<String> getResources() {
		return ResponseEntity.ok("Resources accessed with READ:RESOURCE permission");
	}
}
