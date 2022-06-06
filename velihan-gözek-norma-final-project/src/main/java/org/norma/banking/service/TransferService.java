package org.norma.banking.service;


import org.norma.banking.dto.MoneyTransferDto;
import org.norma.banking.external.ExchangeRates;
import org.norma.banking.entity.Transfer;

import java.util.List;

public interface TransferService {

    ExchangeRates exchangeRate();

    double usdRate();

    double eurRate();

    List<Transfer> findAll();

    Transfer sendMoney(MoneyTransferDto moneyTransferDto, String iban1, String iban2);

}
