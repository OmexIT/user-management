package com.example.usermanagement.shared.exception;

public class KeycloakIntegrationException extends RuntimeException {

	public KeycloakIntegrationException(String message) {
		super(message);
	}

	public KeycloakIntegrationException(String message, Throwable cause) {
		super(message, cause);
	}
}
