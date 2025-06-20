package com.example.usermanagement.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@PreAuthorize("hasRole('CUSTOMER') and hasPermission(null, 'WRITE') and hasPermission(null, 'READ')")
	@GetMapping("/customerRole")
	public String getCustomerRole() {
		return "User has customer role.";
	}
}
