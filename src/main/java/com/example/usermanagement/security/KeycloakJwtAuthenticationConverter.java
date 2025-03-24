package com.example.usermanagement.security;

import com.example.usermanagement.service.PermissionMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private final PermissionMappingService permissionMappingService;
	private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	{
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
	}

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		// Extract standard authorities using Spring's converter
		Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);

		// Extract roles from authorities
		Set<String> roles = authorities.stream().map(GrantedAuthority::getAuthority)
				.filter(authority -> authority.startsWith("ROLE_")).map(authority -> authority.substring(5)) // Remove
																												// "ROLE_"
																												// prefix
				.collect(Collectors.toSet());

		// Get permissions for these roles
		Set<String> permissions = permissionMappingService.getPermissionsForRoles(roles);

		// Add permissions as authorities
		Set<GrantedAuthority> allAuthorities = new HashSet<>(authorities);
		permissions.forEach(permission -> allAuthorities.add(new SimpleGrantedAuthority("PERMISSION_" + permission)));

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

		return new JwtAuthenticationToken(jwt, allAuthorities, principal.getSubject());
	}
}
