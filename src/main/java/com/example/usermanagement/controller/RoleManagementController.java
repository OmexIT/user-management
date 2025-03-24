package com.example.usermanagement.controller;

import com.example.usermanagement.dto.RoleDto;
import com.example.usermanagement.mapper.RoleMapper;
import com.example.usermanagement.service.KeycloakRoleService;
import com.example.usermanagement.service.PermissionMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller for role management operations Following the
 * Controller-Service-Repository pattern with DTOs for clean separation
 */
@RestController
@RequestMapping("/api/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class RoleManagementController {

	private final KeycloakRoleService keycloakRoleService;
	private final PermissionMappingService permissionMappingService;
	private final RoleMapper roleMapper;

	/**
	 * Get all roles
	 */
	@GetMapping
	public ResponseEntity<List<RoleDto>> getAllRoles() {
		List<RoleRepresentation> roles = keycloakRoleService.getAllRoles();
		List<RoleDto> roleDtos = roleMapper.toDTOList(roles);
		return ResponseEntity.ok(roleDtos);
	}

	/**
	 * Get a specific role
	 */
	@GetMapping("/{roleName}")
	public ResponseEntity<RoleDto> getRole(@PathVariable String roleName) {
		RoleRepresentation role = keycloakRoleService.getRole(roleName);
		if (role == null) {
			return ResponseEntity.notFound().build();
		}

		// Get permissions for the role
		Set<String> permissions = permissionMappingService.getPermissionsForRole(roleName);
		RoleDto roleDTO = roleMapper.toDTO(role, permissions);

		return ResponseEntity.ok(roleDTO);
	}

	/**
	 * Create a new role
	 */
	@PostMapping
	public ResponseEntity<Void> createRole(@RequestBody RoleDto roleDTO) {
		// Create role in Keycloak
		keycloakRoleService.createRole(roleDTO.name(), roleDTO.description());

		// If permissions are provided, add them to the role
		if (roleDTO.permissions() != null && !roleDTO.permissions().isEmpty()) {
			keycloakRoleService.addPermissionsToRole(roleDTO.name(), roleDTO.permissions());
		}

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * Get permissions for a role
	 */
	@GetMapping("/{roleName}/permissions")
	public ResponseEntity<Set<String>> getRolePermissions(@PathVariable String roleName) {
		Set<String> permissions = permissionMappingService.getPermissionsForRole(roleName);
		return ResponseEntity.ok(permissions);
	}

	/**
	 * Add permissions to a role
	 */
	@PostMapping("/{roleName}/permissions")
	public ResponseEntity<Void> addPermissionsToRole(@PathVariable String roleName,
			@RequestBody Set<String> permissions) {
		keycloakRoleService.addPermissionsToRole(roleName, permissions);
		permissionMappingService.refreshRolePermissionsCache(roleName);
		return ResponseEntity.noContent().build();
	}
}
