package com.example.usermanagement.user.mapper;

import com.example.usermanagement.user.dto.UserDto;
import com.example.usermanagement.user.model.User;
import com.example.usermanagement.shared.security.UserPrincipal;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

/**
 * Mapper interface for converting between User entity and UserDTO Using
 * MapStruct for automatic code generation
 */
@Mapper
public interface UserMapper {

	/**
	 * Convert User entity to UserDTO with roles and permissions
	 */
	@Mapping(target = "roles", source = "roles")
	@Mapping(target = "permissions", source = "permissions")
	UserDto toDto(User user, Set<String> roles, Set<String> permissions);

	/**
	 * Convert User entity to UserDTO without roles and permissions Uses empty sets
	 * for roles and permissions
	 */
	@Mapping(target = "roles", expression = "java(java.util.Collections.emptySet())")
	@Mapping(target = "permissions", expression = "java(java.util.Collections.emptySet())")
	UserDto toDto(User user);

	/**
	 * Convert UserPrincipal to UserDTO
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "externalId", source = "subject")
	@Mapping(target = "displayName", source = "email") // Use email as display name if not available
	UserDto toDto(UserPrincipal principal);

	/**
	 * Convert list of User entities to list of UserDTOs
	 */
	List<UserDto> toDTOList(List<User> users);

	/**
	 * After mapping, ensure non-null collections
	 */
	@AfterMapping
	default void ensureNonNullCollections(@MappingTarget UserDto userDto) {
		if (userDto.roles() == null) {
			userDto = UserDto.builder().id(userDto.id()).externalId(userDto.externalId()).email(userDto.email())
					.displayName(userDto.displayName()).userType(userDto.userType()).status(userDto.status())
					.roles(Set.of()).permissions(userDto.permissions() != null ? userDto.permissions() : Set.of())
					.build();
		}

		if (userDto.permissions() == null) {
			userDto = UserDto.builder().id(userDto.id()).externalId(userDto.externalId()).email(userDto.email())
					.displayName(userDto.displayName()).userType(userDto.userType()).status(userDto.status())
					.roles(userDto.roles() != null ? userDto.roles() : Set.of()).permissions(Set.of()).build();
		}
	}
}
