package com.example.usermanagement.user.repository;

import com.example.usermanagement.user.model.PortalUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortalUserRepository extends CrudRepository<PortalUser, Long> {

	Optional<PortalUser> findByUserId(Long userId);

	Optional<PortalUser> findByEmployeeId(String employeeId);

	boolean existsByEmployeeId(String employeeId);

	Iterable<PortalUser> findByDepartment(String department);

	Iterable<PortalUser> findByAccessLevel(String accessLevel);

	Iterable<PortalUser> findByCanImpersonateTrue();
}
