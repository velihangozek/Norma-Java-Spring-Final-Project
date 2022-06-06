package org.norma.banking.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.banking.dto.MoneyTransferDto;
import org.norma.banking.entity.Account;
import org.norma.banking.external.ExchangeRates;
import org.norma.banking.entity.Transfer;
import org.norma.banking.repository.AccountRepository;
import org.norma.banking.repository.MoneyTransferRepository;
import org.norma.banking.service.TransferService;
import org.norma.banking.transformer.MoneyTransferTransformer;
import org.norma.banking.external.NumberEvents;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl extends NumberEvents implements TransferService {


    private final RestTemplate restTemplate;


    private final MoneyTransferTransformer moneyTransferTransformer;


    private final MoneyTransferRepository moneyTransferRepository;


    private final AccountRepository accountRepository;



    @Override
    public ExchangeRates exchangeRate() {
        return restTemplate.getForObject("https://api.exchangeratesapi.io/latest?base=TRY", ExchangeRates.class);
    }

    @Override
    public double usdRate() {
        return exchangeRate().getRates().get("TRY") / exchangeRate().getRates().get("USD");
    }

    @Override
    public double eurRate() {
        return exchangeRate().getRates().get("TRY") / exchangeRate().getRates().get("EUR");
    }

    @Override
    public List<Transfer> findAll() {
        return moneyTransferRepository.findAll();
    }


    @Override
    @Transactional(rollbackFor = ResponseStatusException.class)
    public Transfer sendMoney(MoneyTransferDto moneyTransferDto, String senderIban, String receiverIban) {

            Transfer transfer = moneyTransferTransformer.accountTransfer(moneyTransferDto);
            double amount = transfer.getAmount();
            String currency = transfer.getCurrency();
            String accountType = transfer.getAccountType();
            senderIban = transfer.getSenderIban();
            receiverIban = transfer.getReceiverIban();
            toAccountsTransfer(transfer, amount, senderIban, receiverIban);
            Transfer transfer1 = new Transfer();
            saveTransfer(transfer1, amount, currency, accountType,senderIban,receiverIban);
            return transfer;
    }

    private void saveTransfer(Transfer transfer, double amount, String currency, String accountType, String senderIban, String receiverIban){
        transfer.setAmount(amount);
        transfer.setCurrency(currency);
        transfer.setAccountType(accountType);
        transfer.setSenderIban(senderIban);
        transfer.setReceiverIban(receiverIban);
        moneyTransferRepository.save(transfer);
    }

    private void toAccountsTransfer(Transfer transfer, double amount, String senderIban, String receiverIban){

        String euro = "EUR";
        String tl = "TRY";
        String usd = "USD";

        Account accountSend = transfer.getAccounts().get(0);
        Account accountTake = transfer.getAccounts().get(1);

        double forEurToTl = (double) Math.round((amount * eurRate()) * 100) / 100;
        double forTlToEuro = (double) Math.round((amount / eurRate()) * 100) / 100;

        double forUsdToTl = (double) Math.round((amount * usdRate()) * 100) / 100;
        double forTlToUsd = (double) Math.round((amount / usdRate()) * 100) / 100;

        if (senderIban.equals(accountSend.getIban()) & receiverIban.equals(accountTake.getIban()) & accountSend.getAccountType().equals("CHECKING")){

                if (accountSend.getAccountType().equals(accountTake.getAccountType()) && accountSend.getCurrency().equals(accountTake.getCurrency())) {
                    accountSend.setBalance(accountSend.getBalance() - amount);
                    accountTake.setBalance(accountTake.getBalance() + amount);
                }
                if (accountSend.getCurrency().equals(euro) & accountTake.getCurrency().equals(tl)){
                    accountSend.setBalance(accountSend.getBalance() - amount);
                    accountTake.setBalance(accountTake.getBalance() + forEurToTl);
                }
                else if (accountSend.getCurrency().equals(tl) & accountTake.getCurrency().equals(euro)){
                    accountSend.setBalance(accountSend.getBalance() - amount);
                    accountTake.setBalance(accountTake.getBalance() + forTlToEuro);
                }else if (accountSend.getCurrency().equals(usd) & accountTake.getCurrency().equals(tl)){
                    accountSend.setBalance(accountSend.getBalance() - amount);
                    accountTake.setBalance(accountTake.getBalance() + forUsdToTl);
                }else if (accountSend.getCurrency().equals(tl) & accountTake.getCurrency().equals(usd)){
                    accountSend.setBalance(accountSend.getBalance() - amount);
                    accountTake.setBalance(accountTake.getBalance() + forTlToUsd);
                }else if (accountSend.getCurrency().equals(euro) & accountTake.getCurrency().equals(usd)){
                    double euroAmount = amount * (usdRate() / eurRate());
                    double usdAmount = amount * (eurRate() / usdRate());
                    accountSend.setBalance(accountSend.getBalance() - euroAmount);
                    accountTake.setBalance(accountTake.getBalance() + usdAmount);
                }else if (accountSend.getCurrency().equals(usd) & accountTake.getCurrency().equals(euro)){
                    double euroAmount = amount * (usdRate() / eurRate());
                    double usdAmount = amount * (eurRate() / usdRate());
                    accountSend.setBalance(accountSend.getBalance() - usdAmount);
                    accountTake.setBalance(accountTake.getBalance() + euroAmount);
                }
        }else if (senderIban.equals(accountSend.getIban()) & receiverIban.equals(accountTake.getIban()) &
                accountSend.getAccountType().equals("SAVING")){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Transferring money from a saving account is not allowed!");
        }
        accountRepository.save(accountSend);
        accountRepository.save(accountTake);
    }

}