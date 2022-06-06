package org.norma.banking.repository;

import org.norma.banking.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface AccountRepository extends CrudRepository<Account, Integer> {


    Page<Account> findAll(Pageable pageable);

    Account getById(int id);

}
