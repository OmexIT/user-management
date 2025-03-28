package com.example.usermanagement.controller;

import com.example.usermanagement.dto.CustomerDto;
import com.example.usermanagement.model.Customer;
import com.example.usermanagement.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for customer management operations Following the
 * Controller-Service-Repository pattern
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

	private final CustomerService customerService;

	/**
	 * Create a new customer with user
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
		log.info("Creating customer with number: {}", customerDto.customerNumber());
		Customer customer = customerService.createCustomer(customerDto);

		// Build response DTO
		CustomerDto responseDto = CustomerDto.builder().id(customer.getId())
				.customerNumber(customer.getCustomerNumber()).name(customer.getName()).type(customer.getType()).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	/**
	 * Get all customers
	 */
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CustomerDto>> getAllCustomers() {
		Iterable<Customer> customers = customerService.getAllCustomers();
		List<CustomerDto> customerDtos = new ArrayList<>();

		customers.forEach(customer -> {
			customerDtos.add(CustomerDto.builder().id(customer.getId()).customerNumber(customer.getCustomerNumber())
					.name(customer.getName()).type(customer.getType()).build());
		});

		return ResponseEntity.ok(customerDtos);
	}

	/**
	 * Get customer by ID
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
		Customer customer = customerService.getCustomerById(id);

		CustomerDto customerDto = CustomerDto.builder().id(customer.getId())
				.customerNumber(customer.getCustomerNumber()).name(customer.getName()).type(customer.getType()).build();

		return ResponseEntity.ok(customerDto);
	}

	/**
	 * Get customer by number
	 */
	@GetMapping("/number/{customerNumber}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	public ResponseEntity<CustomerDto> getCustomerByNumber(@PathVariable String customerNumber) {
		Customer customer = customerService.getCustomerByNumber(customerNumber);

		CustomerDto customerDto = CustomerDto.builder().id(customer.getId())
				.customerNumber(customer.getCustomerNumber()).name(customer.getName()).type(customer.getType()).build();

		return ResponseEntity.ok(customerDto);
	}

	/**
	 * Get customer by user ID
	 */
	@GetMapping("/user/{userId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	public ResponseEntity<CustomerDto> getCustomerByUserId(@PathVariable Long userId) {
		Customer customer = customerService.getCustomerByUserId(userId);

		CustomerDto customerDto = CustomerDto.builder().id(customer.getId())
				.customerNumber(customer.getCustomerNumber()).name(customer.getName()).type(customer.getType()).build();

		return ResponseEntity.ok(customerDto);
	}

	/**
	 * Update customer
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id,
			@Valid @RequestBody CustomerDto customerDto) {
		Customer updatedCustomer = customerService.updateCustomer(id, customerDto);

		CustomerDto responseDto = CustomerDto.builder().id(updatedCustomer.getId())
				.customerNumber(updatedCustomer.getCustomerNumber()).name(updatedCustomer.getName())
				.type(updatedCustomer.getType()).build();

		return ResponseEntity.ok(responseDto);
	}

	/**
	 * Customer dashboard endpoint
	 */
	@GetMapping("/dashboard/display")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<String> customerDashboard() {
		return ResponseEntity.ok("Customer Dashboard");
	}
}
