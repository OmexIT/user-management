package com.example.usermanagement.repository;

import com.example.usermanagement.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByExternalId(String externalId);

	Optional<User> findByEmail(String email);

	boolean existsByExternalId(String externalId);

	boolean existsByEmail(String email);
}
