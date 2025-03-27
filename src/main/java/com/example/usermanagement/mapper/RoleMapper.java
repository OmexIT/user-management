package com.example.usermanagement.mapper;

import com.example.usermanagement.dto.RoleDto;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Mapper interface for converting between Keycloak RoleRepresentation and
 * RoleDTO Using MapStruct for automatic code generation
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

	/**
	 * Convert Keycloak RoleRepresentation to RoleDTO Extracts permissions from
	 * attributes
	 */
	@Mapping(target = "permissions", expression = "java(extractPermissions(roleRepresentation))")
	RoleDto toDTO(RoleRepresentation roleRepresentation);

	/**
	 * Convert Keycloak RoleRepresentation to RoleDTO with explicit permissions
	 */
	@Mapping(target = "permissions", source = "permissions")
	RoleDto toDTO(RoleRepresentation roleRepresentation, Set<String> permissions);

	/**
	 * Convert list of Keycloak RoleRepresentations to list of RoleDTOs
	 */
	List<RoleDto> toDTOList(List<RoleRepresentation> roleRepresentations);

	/**
	 * Convert RoleDTO to Keycloak RoleRepresentation Sets permissions as attributes
	 */
	@Mapping(target = "attributes", expression = "java(createAttributesWithPermissions(roleDto.permissions()))")
	RoleRepresentation toRepresentation(RoleDto roleDto);

	/**
	 * Helper method to extract permissions from role attributes
	 */
	default Set<String> extractPermissions(RoleRepresentation roleRepresentation) {
		if (roleRepresentation == null) {
			return Set.of();
		}

		Map<String, List<String>> attributes = roleRepresentation.getAttributes();
		if (attributes == null || !attributes.containsKey("permissions")) {
			return Set.of();
		}

		return new HashSet<>(attributes.get("permissions"));
	}

	/**
	 * Helper method to create attributes map with permissions
	 */
	default Map<String, List<String>> createAttributesWithPermissions(Set<String> permissions) {
		Map<String, List<String>> attributes = new HashMap<>();

		if (permissions != null && !permissions.isEmpty()) {
			attributes.put("permissions", new ArrayList<>(permissions));
		}

		return attributes;
	}
}
