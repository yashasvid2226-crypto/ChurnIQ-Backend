package com.churniq.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByCustomerId(String customerId);

    boolean existsByCustomerIdAndIdNot(String customerId, Long id);

    long countByChurnedTrue();

}