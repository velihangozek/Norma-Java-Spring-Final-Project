package org.norma.banking.transformer;

import org.norma.banking.dto.MoneyTransferDto;
import org.norma.banking.entity.Account;
import org.norma.banking.entity.Transfer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MoneyTransferTransformer {

    public Transfer accountTransfer(MoneyTransferDto moneyTransferDto){

        List<Account> accounts = new ArrayList<>();
        accountDtoCreate(accounts, moneyTransferDto);
        Transfer transfer = new Transfer();
        transferCreate(accounts, moneyTransferDto,transfer);
        return transfer;
    }


    private void transferCreate(List<Account> accounts, MoneyTransferDto moneyTransferDto, Transfer transfer ){

        transfer.setAccountType(moneyTransferDto.getAccountType());
        transfer.setCurrency(moneyTransferDto.getCurrency());
        transfer.setAccounts(accounts);
        transfer.setAmount(moneyTransferDto.getAmount());
        transfer.setDate(LocalDate.now());
        transfer.setSenderIban(moneyTransferDto.getSenderIban());
        transfer.setReceiverIban(moneyTransferDto.getReceiverIban());
    }


    private void accountDtoCreate(List<Account> accounts, MoneyTransferDto moneyTransferDto){
        for (int i = 0; i < moneyTransferDto.getAccounts().size(); i++) {
            Account accountDto = moneyTransferDto.getAccounts().get(i);

            Account account = new Account();
            account.setId(accountDto.getId());
            account.setIban(accountDto.getIban());
            account.setBalance(accountDto.getBalance());
            account.setCurrency(accountDto.getCurrency());
            account.setAccountType(accountDto.getAccountType());
            account.setCreatedDate(accountDto.getCreatedDate());
            accounts.add(account);
        }
    }

}
