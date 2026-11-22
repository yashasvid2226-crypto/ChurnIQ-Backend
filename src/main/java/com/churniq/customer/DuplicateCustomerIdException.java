package com.churniq.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateCustomerIdException extends RuntimeException {

	public DuplicateCustomerIdException(String customerId) {
		super("A customer with customerId '" + customerId + "' already exists");
	}

}
