package com.example.usermanagement.security;

import com.example.usermanagement.service.PermissionMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private final PermissionMappingService permissionMappingService;
	private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

	public KeycloakJwtAuthenticationConverter(PermissionMappingService permissionMappingService) {
		this.permissionMappingService = permissionMappingService;
		this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		this.jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		this.jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
	}

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		log.debug("Converting JWT token: {}", jwt);

		// Extract roles directly from the JWT token
		Set<String> roles = extractRoles(jwt);
		log.debug("Extracted roles: {}", roles);

		// Filter out default roles to avoid Keycloak API calls for them
		Set<String> customRoles = filterDefaultRoles(roles);
		log.debug("Custom roles (after filtering): {}", customRoles);

		// Create authorities from roles
		Set<GrantedAuthority> authorities = new HashSet<>();
		roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
		log.debug("Created authorities: {}", authorities);

		// Get permissions only for custom roles
		Set<String> permissions = customRoles.isEmpty()
				? Set.of()
				: permissionMappingService.getPermissionsForRoles(customRoles);
		log.debug("Retrieved permissions: {}", permissions);

		// Add permissions as authorities
		Set<GrantedAuthority> allAuthorities = new HashSet<>(authorities);
		permissions.forEach(permission -> allAuthorities.add(new SimpleGrantedAuthority("PERMISSION_" + permission)));
		log.debug("All authorities: {}", allAuthorities);

		// Create user principal with all extracted data
		String subject = jwt.getSubject();
		String email = jwt.getClaimAsString("email");

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("roles", roles);
		attributes.put("permissions", permissions);

		// Add custom attributes from token
		if (jwt.hasClaim("user_type")) {
			attributes.put("user_type", jwt.getClaimAsString("user_type"));
		}
		if (jwt.hasClaim("merchant_id")) {
			attributes.put("merchant_id", jwt.getClaimAsString("merchant_id"));
		}
		if (jwt.hasClaim("customer_id")) {
			attributes.put("customer_id", jwt.getClaimAsString("customer_id"));
		}

		UserPrincipal principal = new UserPrincipal(subject, email, roles, permissions, attributes);
		log.debug("Created UserPrincipal: {}", principal);

		return new KeycloakJwtAuthenticationToken(jwt, allAuthorities, principal);
	}

	/**
	 * Extract roles from the JWT token
	 */
	@SuppressWarnings("unchecked")
	private Set<String> extractRoles(Jwt jwt) {
		Set<String> roles = new HashSet<>();

		try {
			Map<String, Object> realmAccess = jwt.getClaim("realm_access");
			if (realmAccess != null && realmAccess.containsKey("roles")) {
				List<String> rolesList = (List<String>) realmAccess.get("roles");
				roles.addAll(rolesList);
			}
		} catch (Exception e) {
			log.error("Error extracting roles from JWT token", e);
		}

		return roles;
	}

	/**
	 * Filter out default Keycloak roles that don't need permission checks
	 */
	private Set<String> filterDefaultRoles(Set<String> roles) {
		// Define a set of known default roles to exclude
		Set<String> defaultRoles = Set.of("default-roles-dfs-tests", "offline_access", "uma_authorization",
				"default-roles-master");

		// Also filter out roles that start with standard prefixes
		return roles.stream().filter(role -> !defaultRoles.contains(role))
				.filter(role -> !role.startsWith("default-roles-")).collect(Collectors.toSet());
	}
}
