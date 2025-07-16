package com.example.usermanagement.user.service;

import com.example.usermanagement.shared.exception.KeycloakIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import com.example.usermanagement.shared.exception.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakRoleService {

	private final RealmResource realmResource;

	/**
	 * Get roles resource for role operations
	 */
	private RolesResource getRolesResource() {
		return realmResource.roles();
	}

	/**
	 * Get all roles in the realm
	 */
	public List<RoleRepresentation> getAllRoles() {
		try {
			return getRolesResource().list();
		} catch (Exception e) {
			log.error("Error fetching roles from Keycloak", e);
			throw new KeycloakIntegrationException("Failed to fetch roles", e);
		}
	}

	/**
	 * Get a specific role by name
	 */
	public RoleRepresentation getRole(String roleName) {
		try {
			return getRolesResource().get(roleName).toRepresentation();
		} catch (NotFoundException e) {
			log.warn("Role not found: {}", roleName);
			return null;
		} catch (Exception e) {
			log.error("Error fetching role from Keycloak: {}", roleName, e);
			throw new KeycloakIntegrationException("Failed to fetch role: " + roleName, e);
		}
	}

	/**
	 * Create a new role
	 */
	public void createRole(String roleName, String description) {
		RoleRepresentation role = new RoleRepresentation();
		role.setName(roleName);
		role.setDescription(description);

		try {
			getRolesResource().create(role);
			log.info("Created role: {}", roleName);
		} catch (Exception e) {
			log.error("Error creating role in Keycloak: {}", roleName, e);
			throw new KeycloakIntegrationException("Failed to create role: " + roleName, e);
		}
	}

	/**
	 * Update role attributes (including permissions)
	 */
	public void updateRoleAttributes(String roleName, Map<String, List<String>> attributes) {
		try {
			RoleResource roleResource = getRolesResource().get(roleName);
			RoleRepresentation role = roleResource.toRepresentation();

			// Update attributes
			role.setAttributes(attributes);

			// Save changes
			roleResource.update(role);
			log.info("Updated attributes for role: {}", roleName);
		} catch (NotFoundException e) {
			log.warn("Role not found for attribute update: {}", roleName);
			throw new EntityNotFoundException("Role not found: " + roleName);
		} catch (Exception e) {
			log.error("Error updating role attributes in Keycloak: {}", roleName, e);
			throw new KeycloakIntegrationException("Failed to update role attributes: " + roleName, e);
		}
	}

	/**
	 * Add permissions to a role (stored as attributes)
	 */
	public void addPermissionsToRole(String roleName, Set<String> permissions) {
		try {
			RoleResource roleResource = getRolesResource().get(roleName);
			RoleRepresentation role = roleResource.toRepresentation();

			// Get existing attributes or create new map
			Map<String, List<String>> attributes = role.getAttributes();
			if (attributes == null) {
				attributes = new HashMap<>();
			}

			// Get existing permissions or create new list
			List<String> existingPermissions = attributes.getOrDefault("permissions", new ArrayList<>());

			// Add new permissions
			Set<String> allPermissions = new HashSet<>(existingPermissions);
			allPermissions.addAll(permissions);

			// Update attributes
			attributes.put("permissions", new ArrayList<>(allPermissions));
			role.setAttributes(attributes);

			// Save changes
			roleResource.update(role);
			log.info("Added permissions to role {}: {}", roleName, permissions);
		} catch (NotFoundException e) {
			log.warn("Role not found for adding permissions: {}", roleName);
			throw new EntityNotFoundException("Role not found: " + roleName);
		} catch (Exception e) {
			log.error("Error adding permissions to role in Keycloak: {}", roleName, e);
			throw new KeycloakIntegrationException("Failed to add permissions to role: " + roleName, e);
		}
	}

	/**
	 * Get all permissions assigned to a role
	 */
	public Set<String> getRolePermissions(String roleName) {
		try {
			RoleRepresentation role = getRolesResource().get(roleName).toRepresentation();

			// Get attributes
			Map<String, List<String>> attributes = role.getAttributes();
			if (attributes == null || !attributes.containsKey("permissions")) {
				return Collections.emptySet();
			}

			// Return permissions
			return new HashSet<>(attributes.get("permissions"));
		} catch (NotFoundException e) {
			log.warn("Role not found for getting permissions: {}", roleName);
			return Collections.emptySet();
		} catch (Exception e) {
			log.error("Error getting permissions for role from Keycloak: {}", roleName, e);
			throw new KeycloakIntegrationException("Failed to get permissions for role: " + roleName, e);
		}
	}
}
