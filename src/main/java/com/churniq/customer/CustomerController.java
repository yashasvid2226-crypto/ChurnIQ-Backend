package com.churniq.customer;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping
	public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer) {
		Customer saved = customerService.create(customer);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(saved.getId())
				.toUri();
		return ResponseEntity.created(location).body(saved);
	}

	@GetMapping
	public List<Customer> findAll() {
		return customerService.findAll();
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return customerService.findById(id);
	}

	@PutMapping("/{id}")
	public Customer update(@PathVariable Long id, @Valid @RequestBody Customer customer) {
		return customerService.update(id, customer);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		customerService.delete(id);
	}

}
