package org.norma.banking.service;

import org.norma.banking.dto.CustomerDto;
import org.norma.banking.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerService {

    CustomerDto createCustomer(CustomerDto customer);

    Page<Customer> listAll(Pageable pageable);

    Customer getById(int id);

    CustomerDto updateCustomer(CustomerDto customerDto, int customerId);

    void deleteCustomer(int id);
}
