package com.example.usermanagement.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Slf4j
public class DynamicPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
		if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
			return false;
		}

        // Check direct permission
		String permissionStr = permission.toString();
		if (principal.hasPermission(permissionStr)) {
			return true;
		}

		// Check for wildcard permissions
		String resourceType = extractResourceType(permissionStr);
		if (resourceType != null) {
			String wildcardPermission = permissionStr.replace(resourceType, "*");
			if (principal.hasPermission(wildcardPermission)) {
				return true;
			}
		}

		// Check for operation wildcards
		String operation = extractOperation(permissionStr);
		if (operation != null && resourceType != null) {
			String operationWildcard = "*:" + resourceType;
			if (principal.hasPermission(operationWildcard)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
		if (targetId == null || targetType == null) {
			return false;
		}

		// For ID-based permissions, we need to check if the user has access to this
		// specific resource
		if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
			return false;
		}

		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		String permissionStr = permission.toString();

		// Check direct permission
		if (principal.hasPermission(permissionStr)) {
			return true;
		}

		// Check scoped permission (e.g., READ:CUSTOMER:123)
		String scopedPermission = permissionStr + ":" + targetId;
		if (principal.hasPermission(scopedPermission)) {
			return true;
		}

		// Check merchant-scoped permission for merchant users
		if (principal.hasAttribute("merchant_id") && targetType.equalsIgnoreCase("MERCHANT")) {
			String merchantId = principal.getAttribute("merchant_id").toString();
			if (merchantId.equals(targetId.toString())) {
				// User belongs to this merchant, check merchant-scoped permission
				String merchantScopedPermission = permissionStr + ":OWN";
				if (principal.hasPermission(merchantScopedPermission)) {
					return true;
				}
			}
		}

		return false;
	}

	private String extractResourceType(String permission) {
		String[] parts = permission.split(":");
		return parts.length >= 2 ? parts[1] : null;
	}

	private String extractOperation(String permission) {
		String[] parts = permission.split(":");
		return parts.length >= 1 ? parts[0] : null;
	}
}
