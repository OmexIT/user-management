package com.example.usermanagement.shared.exception;

public class EntityNotFoundException extends RuntimeException {

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(String entityName, Long id) {
		super(String.format("%s not found with id: %d", entityName, id));
	}

	public EntityNotFoundException(String entityName, String identifier) {
		super(String.format("%s not found with identifier: %s", entityName, identifier));
	}
}
