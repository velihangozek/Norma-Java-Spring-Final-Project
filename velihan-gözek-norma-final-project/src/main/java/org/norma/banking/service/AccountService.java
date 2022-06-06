package org.norma.banking.service;

import org.norma.banking.dto.AccountDto;
import org.norma.banking.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AccountService {

    Page<Account> listPageAccount(Pageable pageable);

    void deleteAccount(int id);

    Account getById(int id);

    void addAccount(int id, AccountDto accountDto);

}
