package org.norma.banking.service.impl;

import org.norma.banking.dto.CreditCardDto;
import org.norma.banking.entity.Account;
import org.norma.banking.entity.Card;
import org.norma.banking.entity.Customer;
import org.norma.banking.entity.Expenses;
import org.norma.banking.repository.CreditCardRepository;
import org.norma.banking.repository.CustomerRepository;
import org.norma.banking.service.CreditCardService;
import org.norma.banking.external.NumberEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class CreditCardServiceImpl extends NumberEvents implements CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Page<Card> list(Pageable pageable) {
        return creditCardRepository.findAll(pageable);
    }

    @Override
    public void addCard(int id, CreditCardDto creditCardDto) {

        Customer customer = customerRepository.getById(id);
        List<Card> cards = customer.getCards();
        Card card = new Card();

        if (setNewCard(cards,card,customer, creditCardDto)){
            customerRepository.save(customer);
        }
    }

    private boolean setNewCard(List<Card> cards, Card card, Customer customer, CreditCardDto creditCardDto){
        List<Account> accounts = customer.getAccounts();

        for (int i = 0; i < cards.size(); i++) {
            card.setCardNo(createCardNo());
            card.setCardLimit(creditCardDto.getCardLimit());
            card.setCcv(ccvNo());
            card.setCardType(creditCardDto.getCardType());
            card.setCardPassword(creditCardDto.getCardPassword());
            card.setAccounts(accounts);

            customer.setCards(cards);
        }
        cards.add(card);
        return true;
    }


    @Override
    public HashMap<String,Double> findAllCardDebt(int id) {

        Customer customer = customerRepository.getById(id);
        List<Card> cards = customer.getCards();
        HashMap<String, Double> myJson = new HashMap<>();

        for (Card card : cards) {
            if (card.getCardType().equals("CREDIT")){
                double debtAmount = card.getCardDebt();
                myJson.put(card.getCardType()+" CARD DEPT",debtAmount);
            }
        }
        return myJson;
    }

    @Override
    public Card getById(int id) {
        return creditCardRepository.getById(id);
    }

    @Override
    public Card debtPaymentFromAccount(int id, CreditCardDto creditCardDto) {

        Card card = creditCardRepository.getById(id);
        List<Account> accounts = card.getAccounts();

        for (Account account : accounts) {
            if (account.getAccountType().equals("CHECKING")) {
                double amount = creditCardDto.getAmount();
                card.setCardDebt(card.getCardDebt() - amount);
                card.setCardLimit(card.getCardLimit() + amount);
                account.setBalance(account.getBalance() - amount);
            }
        }
        creditCardRepository.save(card);
        return card;
    }

    @Override
    public Card debtPaymentFromCashpoint(int id, CreditCardDto creditCardDto) {

        try {
            Card card = creditCardRepository.getById(id);

            if (card.getCardPassword().equals(creditCardDto.getCardPassword())){
                double amount = creditCardDto.getAmount();
                card.setCardLimit(card.getCardLimit() + amount);
                card.setCardDebt(card.getCardDebt() - amount);
                creditCardRepository.save(card);
            }
            return card;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Password of the card is wrong!");
        }
    }

    @Override
    public List<Expenses> listReceipt(int id) {
        Card card = creditCardRepository.getById(id);
        return card.getExpenses();
    }

    @Override
    public Card transferMoneyForShopping(int id, CreditCardDto creditCardDto){

        Customer customer = customerRepository.getById(id);
        List<Card> cards = customer.getCards();

        for (int i = 0; i < cards.size(); i++) {
            Card card = customer.getCards().get(i);
            double amount = creditCardDto.getAmount();
            int ccv = creditCardDto.getCcv();
            String cardNo = creditCardDto.getCardNo();
            String productName = creditCardDto.getExpenses().get(0).getProductName();
            String productType = creditCardDto.getExpenses().get(0).getProductType();
            toShoppingFromCard(card, amount, cardNo, ccv, productName, productType);
        }
        return cards.get(0);
    }

    private void toShoppingFromCard(Card card, double amount, String cardNo, int ccv, String pName, String pType){

        try {
            List<Expenses> expenses = card.getExpenses();
            if (card.getCardNo().equals(cardNo) & card.getCcv() == ccv){
                card.setAmount(amount);
                Expenses expensesInfo = new Expenses();
                if (card.getCardType().equals("CREDIT")){

                    for (int i = 0; i < expenses.size(); i++) {

                        expensesInfo.setPrice(amount);
                        expensesInfo.setProductName(pName);
                        expensesInfo.setProductType(pType);
                    }

                    card.setCardDebt(card.getCardDebt() + amount);
                    card.setCardLimit(card.getCardLimit() - amount);

                    expenses.add(expensesInfo);
                }
                else if (card.getCardType().equals("PREPAID")){

                    for (int i = 0; i < expenses.size(); i++) {

                        expensesInfo.setPrice(amount);
                        expensesInfo.setProductName(pName);
                        expensesInfo.setProductType(pType);
                    }

                    card.setCardLimit(card.getCardLimit() - amount);
                    expenses.add(expensesInfo);
                }

                card.setExpenses(expenses);

                creditCardRepository.save(card);
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Card number or cvv is wrong!");
        }
    }
}
