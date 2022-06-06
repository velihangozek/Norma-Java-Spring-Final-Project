package org.norma.banking.repository;

import org.norma.banking.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    Page<Customer> findAll(Pageable page);

    Customer getById(int id);

}
