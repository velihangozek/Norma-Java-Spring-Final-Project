package org.norma.banking.transformer;

import org.norma.banking.dto.AccountDto;
import org.norma.banking.dto.CreditCardDto;
import org.norma.banking.dto.CustomerDto;
import org.norma.banking.entity.Account;
import org.norma.banking.entity.Card;
import org.norma.banking.entity.Customer;
import org.norma.banking.external.NumberEvents;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerTransformer extends NumberEvents {

    public Customer customerTransfer(CustomerDto customerDto) {
        List<Card> cards = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        accountDtoCreate(accounts, customerDto);
        Customer customer = new Customer();
        customerCreate(accounts,customerDto,customer, cards);
        cardDtoCreate(cards, customerDto, customer);
        return customer;
    }

    private void customerCreate(List<Account> accounts, CustomerDto customerDto, Customer customer, List<Card> cards){
        customer.setAccounts(accounts);
        customer.setDescription(customerDto.getDescription());
        customer.setAddress(customerDto.getAddress());
        customer.setEmail(customerDto.getEmail());
        customer.setFullName(customerDto.getFullName());
        customer.setIdentityNumber(customerDto.getIdentityNumber());
        customer.setPassword(customerDto.getPassword());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setCreatedDate(LocalDate.now());
        customer.setCards(cards);
    }

    private void accountDtoCreate(List<Account> accounts, CustomerDto customerDto) {

        for (int i = 0; i < customerDto.getAccounts().size(); i++) {

            AccountDto accountDto = customerDto.getAccounts().get(i);

            Account account = new Account();
            account.setAccountType(accountDto.getAccountType());
            account.setCurrency(accountDto.getCurrency());
            account.setBalance(accountDto.getBalance());
            account.setIban(setAccountIban(account));
            accounts.add(account);
        }
    }

    private void cardDtoCreate(List<Card> cards, CustomerDto customerDto, Customer customer){
        for (int i = 0; i < customerDto.getCreditCards().size(); i++) {

            CreditCardDto creditCardDto = customerDto.getCreditCards().get(i);

            Card card = new Card();
            card.setCardNo(createCardNo());
            card.setCardLimit(creditCardDto.getCardLimit());
            card.setCcv(ccvNo());
            card.setCardType(creditCardDto.getCardType());
            card.setCardDebt(creditCardDto.getCardDebt());
            card.setExpenses(creditCardDto.getExpenses());
            card.setAmount(creditCardDto.getAmount());
            card.setCreatedDate(LocalDate.now());
            card.setCardPassword(creditCardDto.getCardPassword());
            card.setAccounts(customer.getAccounts());
            cards.add(card);
        }
    }

    public CustomerDto toCustomerDto(Customer customer){
        return CustomerDto.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .identityNumber(customer.getIdentityNumber())
                .password(customer.getPassword())
                .description(customer.getDescription())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .creditCards(toCardDtoList(customer.getCards()))
                .accounts(toAccountDtoList(customer.getAccounts()))
                .createdDate(LocalDate.now())
                .build();
    }

    private AccountDto toAccountDto(Account account){
        return AccountDto.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .accountType(account.getAccountType())
                .iban(account.getIban())
                .createdDate(account.getCreatedDate())
                .build();
    }

    public List<AccountDto> toAccountDtoList(List<Account> accounts){
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountDtos.add(toAccountDto(account));
        }
        return accountDtos;
    }


    private CreditCardDto toCardDto(Card card){
        return CreditCardDto.builder()
                .id(card.getId())
                .cardNo(card.getCardNo())
                .cardType(card.getCardType())
                .cardLimit(card.getCardLimit())
                .expiredDate(card.getExpiredDate())
                .ccv(card.getCcv())
                .createdDate(LocalDate.now())
                .cardDebt(card.getCardDebt())
                .amount(card.getAmount())
                .expenses(card.getExpenses())
                .cardPassword(card.getCardPassword())
                .accounts(toAccountDtoList(card.getAccounts()))
                .build();
    }

    public List<CreditCardDto> toCardDtoList(List<Card> cards){
        List<CreditCardDto> creditCardDtos = new ArrayList<>();
        for (Card card : cards) {
            creditCardDtos.add(toCardDto(card));
        }
        return creditCardDtos;
    }

}
