package com.churniq.customer;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Transactional
	public Customer create(Customer customer) {
		customer.setId(null);
		if (customerRepository.existsByCustomerId(customer.getCustomerId())) {
			throw new DuplicateCustomerIdException(customer.getCustomerId());
		}
		try {
			Customer saved = customerRepository.saveAndFlush(customer);
			return saved;
		}
		catch (DataIntegrityViolationException ex) {
			throw new DuplicateCustomerIdException(customer.getCustomerId());
		}
	}

	@Transactional(readOnly = true)
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Customer findById(Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(id));
	}

	@Transactional
	public Customer update(Long id, Customer changes) {
		Customer existing = findById(id);
		if (customerRepository.existsByCustomerIdAndIdNot(changes.getCustomerId(), id)) {
			throw new DuplicateCustomerIdException(changes.getCustomerId());
		}
		existing.setCustomerId(changes.getCustomerId());
		existing.setGender(changes.getGender());
		existing.setTenure(changes.getTenure());
		existing.setContract(changes.getContract());
		existing.setMonthlyCharges(changes.getMonthlyCharges());
		existing.setTotalCharges(changes.getTotalCharges());
		existing.setChurned(changes.getChurned());
		try {
			return customerRepository.saveAndFlush(existing);
		}
		catch (DataIntegrityViolationException ex) {
			throw new DuplicateCustomerIdException(changes.getCustomerId());
		}
	}

	@Transactional
	public void delete(Long id) {
		if (!customerRepository.existsById(id)) {
			throw new CustomerNotFoundException(id);
		}
		customerRepository.deleteById(id);
	}

}
