package com.example.usermanagement.config;

import com.example.usermanagement.shared.security.DynamicPermissionEvaluator;
import com.example.usermanagement.shared.security.KeycloakJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;
	private final DynamicPermissionEvaluator permissionEvaluator;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.oauth2ResourceServer(
				oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/public/**").permitAll()
						.requestMatchers("/api/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
						.requestMatchers("/api/merchants/**").hasAnyRole("MERCHANT", "ADMIN")
						.requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated());
		return http.build();
	}

	@Bean
	public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(permissionEvaluator);
		return expressionHandler;
	}
}
