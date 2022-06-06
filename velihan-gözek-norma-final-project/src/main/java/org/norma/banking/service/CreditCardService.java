package org.norma.banking.service;

import org.norma.banking.dto.CreditCardDto;
import org.norma.banking.entity.Card;
import org.norma.banking.entity.Expenses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

public interface CreditCardService {

    Page<Card> list(Pageable pageable);

    void addCard(int id, CreditCardDto creditCardDto);

    HashMap<String,Double> findAllCardDebt(int id);

    Card transferMoneyForShopping(int id, CreditCardDto creditCardDto);

    Card getById(int id);

    Card debtPaymentFromAccount(int id, CreditCardDto creditCardDto);

    Card debtPaymentFromCashpoint(int id, CreditCardDto creditCardDto);

    List<Expenses> listReceipt(int id);

}
