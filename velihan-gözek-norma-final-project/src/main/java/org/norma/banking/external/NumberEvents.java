package org.norma.banking.external;

import org.norma.banking.entity.Account;

import java.util.Random;

public class NumberEvents {

    enum Currency{
        TRY,
        USD,
        EUR
    }

    private static final int cardNoCount = 16;
    private static final int cardFieldCount = 4;
    private static final int ccvNo = 3;

    private static final int ibanNoCount = 12;
    private static final int ibanFieldCount = 4;

    public <T> String  createIban(String t) {
        String ibanNo = t;
        Random value = new Random();

        int r1 = value.nextInt(10);
        int r2 = value.nextInt(10);
        ibanNo += Integer.toString(r1) + Integer.toString(r2) + " ";

        int count = 0;
        int n = 0;
        for (int i = 0; i < ibanNoCount; i++) {
            if (count == ibanFieldCount) {
                ibanNo += " ";
                count = 0;
            } else
                n = value.nextInt(10);
            ibanNo += Integer.toString(n);
            count++;
        }
        return ibanNo;
    }

    public String setAccountIban(Account account) {
        try {
            if (account.getIban() == null){
                if (account.getCurrency().equals(Currency.TRY.toString())) {
                    account.setIban(createIban("TR"));
                    return account.getIban();
                } else if (account.getCurrency().equals(Currency.USD.toString())) {
                    account.setIban(createIban("US"));
                    return account.getIban();
                } else if (account.getCurrency().equals(Currency.EUR.toString())) {
                    account.setIban(createIban("EU"));
                    return account.getIban();
                }
            }
        }catch (IllegalArgumentException exception){
            System.out.println("Only 3 types of currencies exist, including: TRY, USD and EUR");
        }catch (NullPointerException exception){
            System.out.println("null pointer exception");
        }
        return account.getIban();
    }

    public String createCardNo(){
        String cardNo;
        Random value = new Random();
        cardNo = "";
        int count = 0;
        int n = 0;
        for (int i = 0; i < cardNoCount; i++) {
            if (count == cardFieldCount) {
                cardNo += " ";
                count = 0;
            } else
                n = value.nextInt(10);
            cardNo += Integer.toString(n);
            count++;
        }
        return cardNo;
    }

    public int ccvNo(){
        String ccv;
        Random value = new Random();
        ccv = "";
        int count = 3;
        int n = 0;
        for (int i = 0; i < ccvNo; i++) {
            n = value.nextInt(10);
            ccv += Integer.toString(n);
            count--;
        }
        if (ccv.length() == 2){
            int last = value.nextInt(10);
            ccv += Integer.toString(last);
        }

        return Integer.parseInt(ccv);
    }


    }