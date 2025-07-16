package com.example.usermanagement.shared.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Custom JWT authentication token that includes the UserPrincipal
 */
public class KeycloakJwtAuthenticationToken extends JwtAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private final UserPrincipal userPrincipal;

	public KeycloakJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities,
			UserPrincipal userPrincipal) {
		super(jwt, authorities, userPrincipal.getSubject());
		this.userPrincipal = userPrincipal;
	}

	@Override
	public Object getPrincipal() {
		return userPrincipal;
	}
}
