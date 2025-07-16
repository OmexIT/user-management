package com.example.usermanagement.user.service;

import com.example.usermanagement.shared.exception.EntityNotFoundException;
import com.example.usermanagement.shared.exception.EntityExistsException;
import com.example.usermanagement.user.model.Permission;
import com.example.usermanagement.user.model.Role;
import com.example.usermanagement.user.model.RolePermission;
import com.example.usermanagement.user.repository.PermissionRepository;
import com.example.usermanagement.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

	private final PermissionRepository permissionRepository;
	private final RoleRepository roleRepository;

	@Transactional
	public Permission createPermission(String permissionCode, String resource, String action, String description) {
		if (permissionRepository.existsByPermissionCode(permissionCode)) {
			throw new EntityExistsException("Permission", "code", permissionCode);
		}

		Permission permission = new Permission(permissionCode, resource, action, description);
		return permissionRepository.save(permission);
	}

	public Permission getPermissionById(Long id) {
		return permissionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Permission", id));
	}

	public Permission getPermissionByCode(String permissionCode) {
		return permissionRepository.findByPermissionCode(permissionCode)
				.orElseThrow(() -> new EntityNotFoundException("Permission", permissionCode));
	}

	public Iterable<Permission> getAllPermissions() {
		return permissionRepository.findAll();
	}

	public Iterable<Permission> getPermissionsByResource(String resource) {
		return permissionRepository.findByResource(resource);
	}

	public Iterable<Permission> getPermissionsByAction(String action) {
		return permissionRepository.findByAction(action);
	}

	@Transactional
	public void assignPermissionToRole(Long roleId, Long permissionId) {
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Role", roleId));

		Permission permission = getPermissionById(permissionId);

		role.addPermission(permission);
		roleRepository.save(role);
	}

	@Transactional
	public void removePermissionFromRole(Long roleId, Long permissionId) {
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Role", roleId));

		role.removePermission(permissionId);
		roleRepository.save(role);
	}

	public Set<Permission> getPermissionsForRole(Long roleId) {
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Role", roleId));

		Set<Long> permissionIds = role.getRolePermissions().stream().map(RolePermission::getPermissionId)
				.collect(Collectors.toSet());

		List<Permission> permissions = new ArrayList<>();
		permissionRepository.findAllById(permissionIds).forEach(permissions::add);

		return permissions.stream().collect(Collectors.toSet());
	}

	@Transactional
	public Permission updatePermission(Long id, String description, String metadata) {
		Permission permission = getPermissionById(id);

		if (permission.getIsSystem()) {
			throw new IllegalStateException("Cannot modify system permission");
		}

		permission.setDescription(description);
		permission.setMetadata(metadata);

		return permissionRepository.save(permission);
	}

	@Transactional
	public void deletePermission(Long id) {
		Permission permission = getPermissionById(id);

		if (permission.getIsSystem()) {
			throw new IllegalStateException("Cannot delete system permission");
		}

		permissionRepository.deleteById(id);
	}

	public boolean hasPermission(Set<Permission> userPermissions, String resource, String action) {
		return userPermissions.stream().anyMatch(p -> p.getResource().equals(resource) && p.getAction().equals(action));
	}

	public boolean hasWildcardPermission(Set<Permission> userPermissions, String resource, String action) {
		return userPermissions.stream().anyMatch(p -> (p.getResource().equals("*") || p.getResource().equals(resource))
				&& (p.getAction().equals("*") || p.getAction().equals(action)));
	}
}
