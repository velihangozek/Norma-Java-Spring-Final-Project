package org.norma.banking.transformer;

import org.norma.banking.dto.AccountDto;
import org.norma.banking.dto.CreditCardDto;
import org.norma.banking.entity.Account;
import org.norma.banking.entity.Card;
import org.norma.banking.external.NumberEvents;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreditCardTransformer extends NumberEvents {

    public Card cardTransfer(CreditCardDto creditCardDto){
        Card card = new Card();
        toCreateCard(card, creditCardDto);
        return card;
    }


    private void toCreateCard(Card card, CreditCardDto creditCardDto){
        card.setCardNo(createCardNo());
        card.setCcv(ccvNo());
        card.setCardType(creditCardDto.getCardType());
        card.setCardDebt(creditCardDto.getCardDebt());
        card.setCardLimit(creditCardDto.getCardLimit());
        card.setExpenses(creditCardDto.getExpenses());
        card.setAmount(creditCardDto.getAmount());
        card.setCreatedDate(LocalDate.now());
        card.setCardPassword(creditCardDto.getCardPassword());
        card.setAccounts(toAccountList(creditCardDto.getAccounts()));
    }

    public List<Account> toAccountList(List<AccountDto> accountDtos){
        ;List<Account> accounts = new ArrayList<>();
        for (AccountDto accountDto:accountDtos) {
            accounts.add(toAccount(accountDto));
        }
        return accounts;
    }
    private Account toAccount(AccountDto accountDto){
        return Account.builder()
                .id(accountDto.getId())
                .balance(accountDto.getBalance())
                .currency(accountDto.getCurrency())
                .accountType(accountDto.getAccountType())
                .iban(accountDto.getIban())
                .createdDate(accountDto.getCreatedDate())
                .build();
    }

    public List<AccountDto> toAccountDtoList(List<Account> accounts){
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountDtos.add(toAccountDto(account));
        }
        return accountDtos;
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

    public CreditCardDto toCardDto(Card card){
        return CreditCardDto.builder()
                .id(card.getId())
                .cardNo(card.getCardNo())
                .cardType(card.getCardType())
                .cardLimit(card.getCardLimit())
                .expiredDate(card.getExpiredDate())
                .ccv(card.getCcv())
                .createdDate(card.getCreatedDate())
                .cardDebt(card.getCardDebt())
                .amount(card.getAmount())
                .expenses(card.getExpenses())
                .cardPassword(card.getCardPassword())
                .accounts(toAccountDtoList(card.getAccounts()))
                .build();
    }
}
