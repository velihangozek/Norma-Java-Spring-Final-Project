package org.norma.banking.service.impl;

import org.norma.banking.dto.AccountDto;
import org.norma.banking.entity.Account;
import org.norma.banking.entity.Customer;
import org.norma.banking.repository.AccountRepository;
import org.norma.banking.repository.CustomerRepository;
import org.norma.banking.service.AccountService;
import org.norma.banking.external.NumberEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountServiceImpl extends NumberEvents implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Page<Account> listPageAccount(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public void deleteAccount(int id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account getById(int id) {
        return accountRepository.getById(id);
    }

    @Override
    public void addAccount(int id, AccountDto accountDto) {

        Customer customer = customerRepository.getById(id);
        List<Account> accounts = customer.getAccounts();
        Account account = new Account();

        for (int i = 0; i < accounts.size(); i++) {
            account.setAccountType(accountDto.getAccountType());
            account.setBalance(accountDto.getBalance());
            account.setCurrency(accountDto.getCurrency());
            account.setIban(setAccountIban(account));

            customer.setAccounts(accounts);
        }
        accounts.add(account);
        customerRepository.save(customer);
    }


}
