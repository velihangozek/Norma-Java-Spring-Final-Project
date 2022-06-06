package org.norma.banking.service.impl;

import org.norma.banking.dto.CustomerDto;
import org.norma.banking.entity.Account;
import org.norma.banking.entity.Card;
import org.norma.banking.entity.Customer;
import org.norma.banking.repository.CustomerRepository;
import org.norma.banking.service.CustomerService;
import org.norma.banking.transformer.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerTransformer customerTransformer;


    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = customerTransformer.customerTransfer(customerDto);
        customerRepository.save(customer);
        customerDto.setId(customer.getId());
        customerDto.setAccounts(customerTransformer.toAccountDtoList(customer.getAccounts()));
        customerDto.setCreditCards(customerTransformer.toCardDtoList(customer.getCards()));
        customerDto.setCreatedDate(customer.getCreatedDate());
        return customerDto;
    }

    @Override
    public Page<Customer> listAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer getById(int id) {
        return customerRepository.getById(id);
    }

    @Override
    @Transactional
    public CustomerDto updateCustomer(CustomerDto customerDto, int customerId) {

        Customer customer = customerRepository.getById(customerId);

        customer.setFullName(customerDto.getFullName());
        customer.setPassword(customerDto.getPassword());
        customer.setDescription(customerDto.getDescription());
        customer.setEmail(customerDto.getEmail());
        customer.setIdentityNumber(customerDto.getIdentityNumber());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAddress(customerDto.getAddress());

        customerRepository.save(customer);

        return customerDto;

    }

    @Override
    public void deleteCustomer(int id){
        Customer customer = customerRepository.getById(id);

        if (accountBalanceCheck(customer) & cardDebtCheck(customer)){
            customerRepository.deleteById(id);
        }
    }

    private boolean accountBalanceCheck(Customer customer){
        for (int i = 0; i < customer.getAccounts().size(); i++) {
            Account account = customer.getAccounts().get(i);
            if (account.getBalance() > 0){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Customer has funds in her/his account!");
            }
        }
        return true;
    }

    private boolean cardDebtCheck(Customer customer){
        for (int i = 0; i < customer.getCards().size(); i++) {
            Card card = customer.getCards().get(i);
            if (card.getCardDebt() > 0){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Customer has card debt!");
            }
        }
        return true;
    }
}
