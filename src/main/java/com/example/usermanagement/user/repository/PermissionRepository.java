package com.example.usermanagement.user.repository;

import com.example.usermanagement.user.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {

	Optional<Permission> findByPermissionCode(String permissionCode);

	boolean existsByPermissionCode(String permissionCode);

	Iterable<Permission> findByResource(String resource);

	Iterable<Permission> findByAction(String action);

	Iterable<Permission> findByResourceAndAction(String resource, String action);

	Iterable<Permission> findByIsSystemTrue();

	Iterable<Permission> findByIsSystemFalse();
}
