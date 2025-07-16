package com.example.usermanagement.user.dto;

/**
 * DTO for permission check results Using record for immutability and automatic
 * generation of constructors, getters, equals, hashCode, and toString
 */
public record PermissionCheckDto(String permission, boolean hasPermission) {
}
