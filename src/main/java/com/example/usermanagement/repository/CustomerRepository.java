package com.example.usermanagement.repository;

import com.example.usermanagement.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	Optional<Customer> findByUserId(Long userId);

	Optional<Customer> findByCustomerNumber(String customerNumber);

	boolean existsByCustomerNumber(String customerNumber);
}
