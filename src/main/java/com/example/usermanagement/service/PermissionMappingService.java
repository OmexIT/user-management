package com.example.usermanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "rolePermissionsCache")
public class PermissionMappingService {

	private final KeycloakRoleService keycloakRoleService;

	/**
	 * Get permissions for a role with caching
	 */
	@Cacheable(key = "#roleName")
	public Set<String> getPermissionsForRole(String roleName) {
		return keycloakRoleService.getRolePermissions(roleName);
	}

	/**
	 * Get all permissions for multiple roles
	 */
	public Set<String> getPermissionsForRoles(Collection<String> roleNames) {
		return roleNames.stream().flatMap(role -> getPermissionsForRole(role).stream()).collect(Collectors.toSet());
	}

	/**
	 * Refresh the permissions cache for a role
	 */
	@CacheEvict(key = "#roleName")
	public void refreshRolePermissionsCache(String roleName) {
		log.debug("Evicted cache for role: {}", roleName);
	}

	/**
	 * Refresh all role permissions in the cache
	 */
	@CacheEvict(allEntries = true)
	@Scheduled(fixedRateString = "${app.permissions.cache-refresh-rate:300000}")
	public void refreshAllRolePermissions() {
		log.info("Refreshed all role permissions cache");
	}
}
