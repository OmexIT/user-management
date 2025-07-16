package com.example.usermanagement.customer.service;

import com.example.usermanagement.customer.dto.CustomerDto;
import com.example.usermanagement.user.dto.UserDto;
import com.example.usermanagement.customer.model.Customer;
import com.example.usermanagement.user.model.User;
import com.example.usermanagement.model.enums.UserType;
import com.example.usermanagement.customer.repository.CustomerRepository;
import com.example.usermanagement.user.repository.UserRepository;
import com.example.usermanagement.shared.exception.EntityExistsException;
import com.example.usermanagement.shared.exception.EntityNotFoundException;
import com.example.usermanagement.user.service.UserSynchronizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.ws.rs.core.Response;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final UserRepository userRepository;
	private final UserSynchronizationService userSynchronizationService;
	private final RealmResource realmResource;

	/**
	 * Create a new customer with user
	 */
	@Transactional
	public Customer createCustomer(CustomerDto customerDto) {
		// Check if customer number already exists
		if (customerRepository.existsByCustomerNumber(customerDto.customerNumber())) {
			throw new EntityExistsException("Customer with number " + customerDto.customerNumber() + " already exists");
		}

		// Create customer
		Customer customer = new Customer();
		customer.setCustomerNumber(customerDto.customerNumber());
		customer.setName(customerDto.name());
		customer.setType(customerDto.type());

		// Create user in Keycloak if provided
		if (customerDto.user() != null) {
			UserDto userDto = customerDto.user();

			// Create user in Keycloak
			String keycloakId = createKeycloakUser(userDto, "CUSTOMER");

			// Synchronize user to local database
			User user = userSynchronizationService.synchronizeUser(keycloakId, userDto.email(), userDto.displayName(),
					UserType.CUSTOMER, true, new HashMap<>());

			// Save user to get ID
			user = userRepository.save(user);

			// Link customer to user
			customer.setUserId(user.getId());
		}

		// Save customer
		return customerRepository.save(customer);
	}

	/**
	 * Get customer by ID
	 */
	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
	}

	/**
	 * Get customer by number
	 */
	public Customer getCustomerByNumber(String customerNumber) {
		return customerRepository.findByCustomerNumber(customerNumber)
				.orElseThrow(() -> new EntityNotFoundException("Customer not found with number: " + customerNumber));
	}

	/**
	 * Get customer by user ID
	 */
	public Customer getCustomerByUserId(Long userId) {
		return customerRepository.findByUserId(userId)
				.orElseThrow(() -> new EntityNotFoundException("Customer not found for user ID: " + userId));
	}

	/**
	 * Get all customers
	 */
	public Iterable<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	/**
	 * Update customer
	 */
	@Transactional
	public Customer updateCustomer(Long id, CustomerDto customerDto) {
		Customer customer = getCustomerById(id);

		// Update fields
		customer.setName(customerDto.name());
		customer.setType(customerDto.type());

		return customerRepository.save(customer);
	}

	/**
	 * Helper method to create a user in Keycloak
	 */
	private String createKeycloakUser(UserDto userDto, String role) {
		UsersResource usersResource = realmResource.users();

		// Create user representation
		UserRepresentation user = new UserRepresentation();
		user.setEnabled(true);
		user.setUsername(userDto.email());
		user.setEmail(userDto.email());
		user.setFirstName(userDto.displayName());

		// Set credentials
		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(UUID.randomUUID().toString()); // Generate random password
		credential.setTemporary(true); // Require password change on first login

		user.setCredentials(Collections.singletonList(credential));

		// Create user in Keycloak
		Response response = usersResource.create(user);
		String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

		// Assign role
		usersResource.get(userId).roles().realmLevel()
				.add(Collections.singletonList(realmResource.roles().get(role).toRepresentation()));

		return userId;
	}
}
