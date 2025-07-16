package com.example.usermanagement.user.repository;

import com.example.usermanagement.user.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByRoleCode(String roleCode);

	Optional<Role> findByRoleName(String roleName);

	boolean existsByRoleCode(String roleCode);

	Iterable<Role> findByRoleType(String roleType);

	Iterable<Role> findByIsSystemTrue();

	Iterable<Role> findByIsSystemFalse();
}
