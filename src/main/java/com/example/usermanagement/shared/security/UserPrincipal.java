package com.example.usermanagement.shared.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class UserPrincipal {
	private final String subject;
	private final String email;
	private final Set<String> roles;
	private final Set<String> permissions;
	private final Map<String, Object> attributes;

	public boolean hasRole(String role) {
		return roles.contains(role);
	}

	public boolean hasPermission(String permission) {
		return permissions.contains(permission);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	@Override
	public String toString() {
		return "UserPrincipal{" + "subject='" + subject + '\'' + ", email='" + email + '\'' + ", roles=" + roles
				+ ", permissions=" + permissions + ", attributes=" + attributes + '}';
	}
}
