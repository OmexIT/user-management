package com.example.usermanagement.shared.exception;

public class EntityExistsException extends RuntimeException {

	public EntityExistsException(String message) {
		super(message);
	}

	public EntityExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityExistsException(String entityName, String field, String value) {
		super(String.format("%s already exists with %s: %s", entityName, field, value));
	}
}
